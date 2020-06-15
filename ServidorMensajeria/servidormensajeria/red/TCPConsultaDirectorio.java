package servidormensajeria.red;

import emisor.controlador.ControladorEmisor;

import emisor.modelo.IMensaje;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.lang.reflect.Array;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import java.util.TreeSet;

import receptor.modelo.Receptor;


import servidormensajeria.modelo.ISistemaServidor;
import servidormensajeria.modelo.SistemaServidor;

public class TCPConsultaDirectorio {

    private String ipDirectorioPrincipal;
    private int puertoDirectorioPrincipalTiempo;
    private int puertoDirectorioPrincipalDestinatarios;

    private String ipDirectorioSecundario;
    private int puertoDirectorioSecundarioTiempo;
    private int puertoDirectorioSecundarioDestinatarios;


    private Object semaforoActuales = new Object();

    private String ipDirectorioActual;
    private int puertoDirectorioTiempoActual;
    private int puertoDirectorioDestActual;

    private boolean usandoDirSecundario;

    private ISistemaServidor sistemaServidor;


    public TCPConsultaDirectorio(ISistemaServidor sistemaServidor,String ipDirectorioPrincipal, int puertoDirectorioPrincipalTiempo,
                             int puertoDirectorioPrincipalDestinatarios,
                             String ipDirectorioSecundario,
                             int puertoDirectorioSecundarioTiempo, int puertoDirectorioSecundarioDestinatarios) {
        
        this.sistemaServidor = sistemaServidor;
        this.ipDirectorioPrincipal = ipDirectorioPrincipal;
        this.puertoDirectorioPrincipalTiempo = puertoDirectorioPrincipalTiempo;
        this.puertoDirectorioPrincipalDestinatarios = puertoDirectorioPrincipalDestinatarios;
        
        this.ipDirectorioSecundario = ipDirectorioSecundario;
        this.puertoDirectorioSecundarioTiempo = puertoDirectorioSecundarioTiempo;
        this.puertoDirectorioSecundarioDestinatarios = puertoDirectorioSecundarioDestinatarios;
        
        
        this.usandoDirSecundario = true;
        this.cambiarDirectorioActivo();
    }




    /**
     * @param usuarioActual
     * @return null si el receptor no esta conectado o no lo encontró, != null si el receptor esta conectado
     */


    public Receptor getReceptor(String usuarioActual) {
        Receptor r = null;
        try {
            r = pedirReceptor(usuarioActual);
        } catch (Exception e) {
            //e.printStackTrace();
            this.cambiarDirectorioActivo();
            try {
                r = pedirReceptor(usuarioActual);
            } catch (ClassNotFoundException | IOException f) {
                this.cambiarDirectorioActivo();
                System.err.println("estoy mandando nullafangos");
            }
        } 
       
        return r;

    }
    
    
    /**
     * Le pide un receptor al directorio para que se traiga la lista de receptores completa
     * y le envia mensajes asincronicos pendientes a cada receptor conectado
     * 
     * Esta soluciona el caso de:
     * Hay un receptor conectado (al directorio)
     * este receptor tiene mensajes pendientes en la persistencia del sistema de mensajeria
     * El sistema de mensajeria se abre despues que el directorio ya tenga conectado al receptor dado
     * (En este caso el aviso se que se conecto el receptor nunca llegara, por qu ya esta)
     * por si acaso le mand oa todos
     */
        public void envioInicialMensajesAsincronicos() {
            this.getReceptor("");
            System.err.println("el magico????");
            for (Receptor receptor : sistemaServidor.getReceptores()) {
                try {
                    if (receptor.isConectado())
                        this.envioMensajesAsincronicos(receptor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    
    
    public void envioMensajesAsincronicos(Receptor receptor) throws Exception {
        Collection<IMensaje> mensajes = sistemaServidor.obtenerMsjsPendientesReceptor(receptor);
        for (IMensaje mensaje : mensajes) {
            new Thread(new MensajeHandler(mensaje, false,sistemaServidor)).start();
        }
    }
    
    private Receptor pedirReceptor(String usuarioActual) throws IOException, ClassNotFoundException {
        Socket socketTiempo = new Socket();
        
        String IPActual; int puertoTiempoActual; int puertoDestActual;
        synchronized(this.semaforoActuales){
            IPActual=this.ipDirectorioActual;
            puertoTiempoActual = this.puertoDirectorioTiempoActual;
            puertoDestActual = this.puertoDirectorioDestActual;
        }
        
        InetSocketAddress addrTiempo =
            new InetSocketAddress(IPActual, puertoTiempoActual);

        socketTiempo.connect(addrTiempo, 500);
        ObjectInputStream in;

        in = new ObjectInputStream(socketTiempo.getInputStream());


        Long tiempoUltimaActualizacion = (Long) in.readObject();
        in.close();
        socketTiempo.close();

        if (sistemaServidor.getTiempoUltimaActualizacionReceptores() < tiempoUltimaActualizacion) {


            Socket socketDest = new Socket();
            InetSocketAddress addr2 =
                new InetSocketAddress(IPActual, puertoDestActual);
            socketDest.connect(addr2, 500);
            ObjectInputStream inDest = new ObjectInputStream(socketDest.getInputStream());

            sistemaServidor.setReceptores((ArrayList<Receptor>) inDest.readObject());


            inDest.close();
            socketDest.close();


            sistemaServidor.setTiempoUltimaActualizacionReceptores(tiempoUltimaActualizacion);

        }


        ArrayList<Receptor> receptoresArray = sistemaServidor.getReceptores();
        System.out.println("Sobre el crash de nullpointer el usuarioactual es: " + usuarioActual);
        int indice =
            Collections.binarySearch(receptoresArray,
                                     new Receptor("123213", 12312, "AAAAAAAAAA", usuarioActual,
                                                  null)); //este receptor es de mentirita y solo para comparar
        //            System.out.println("INDICE INDICE INDICE " + indice);
        if (indice == -1)
            return null;
        else
            return receptoresArray.get(indice);

    }
    

    



    private void cambiarDirectorioActivo(){
        synchronized(this.semaforoActuales){
            System.out.println("----------------------------------------------------------cambie de directorio");
            if(this.usandoDirSecundario){
                this.ipDirectorioActual = this.ipDirectorioPrincipal;
                this.puertoDirectorioDestActual = this.puertoDirectorioPrincipalDestinatarios;
                this.puertoDirectorioTiempoActual = this.puertoDirectorioPrincipalTiempo;
            }
            else {
                
                this.ipDirectorioActual = this.ipDirectorioSecundario;
                this.puertoDirectorioDestActual = this.puertoDirectorioSecundarioDestinatarios;
                this.puertoDirectorioTiempoActual = this.puertoDirectorioSecundarioTiempo;
            }
            this.usandoDirSecundario = !this.usandoDirSecundario;
        }
        
    }
}
