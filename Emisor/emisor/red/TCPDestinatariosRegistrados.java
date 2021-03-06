package emisor.red;

import emisor.controlador.ControladorEmisor;

import emisor.modelo.ISistemaEmisor;
import emisor.modelo.SistemaEmisor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Iterator;

import receptor.modelo.IDatosReceptor;

import servidormensajeria.modelo.SistemaServidor;

public class TCPDestinatariosRegistrados implements Runnable {
    public static final int TIEMPO_ACTUALIZACION_DESTINATARIOS = 1000; // en MS

    private String IPDirectorio;
    private int puertoDirectorioTiempo;
    private int puertoDirectorioDestinatarios; // en MS
    
    private Long tiempoUltModif = new Long(-999);
    
    
    private boolean usandoDirSecundario;
    private String IPDirectorioActual;
    private int puertoTiempoActual;
    private int puertoDestActual;


    private String ipDirectorioSecundario;
    private int puertoDirectorioSecundarioTiempo;
    private int puertoDirectorioSecundarioDest;

    private ISistemaEmisor sistemaEmisor;


    public TCPDestinatariosRegistrados(ISistemaEmisor sistemaEmisor,String IPDirectorio, int puertoDirectorioTiempo,
                                       int puertoDirectorioDestinatarios, String ipDirectorioSecundario,
                                       int puertoDirectorioSecundarioTiempo, int puertoDirectorioSecundarioDest) {
        
        this.sistemaEmisor = sistemaEmisor;
        
        this.IPDirectorio = IPDirectorio;
        this.puertoDirectorioTiempo = puertoDirectorioTiempo;
        this.puertoDirectorioDestinatarios = puertoDirectorioDestinatarios;
        this.ipDirectorioSecundario = ipDirectorioSecundario;
        this.puertoDirectorioSecundarioTiempo = puertoDirectorioSecundarioTiempo;
        this.puertoDirectorioSecundarioDest = puertoDirectorioSecundarioDest;
        this.usandoDirSecundario = true;
        this.cambiarDirectorioActivo();
    }

    public long getTiempoUltModif() { //para no tener que usar compareTo entre Longs
        return tiempoUltModif;
    }

    @Override
    public void run() {
        while (true) {
            try {


                while (true) {
                    Socket socket = new Socket();
                    InetSocketAddress addr = new InetSocketAddress(IPDirectorioActual, this.puertoTiempoActual);
                    socket.connect(addr, 500);
                    ObjectInputStream inTiempo = new ObjectInputStream(socket.getInputStream());
                    //System.out.println("esperando q me manden el tiempo");
                    Long tiempoUltimaActualizacion;
                    if (socket.isConnected())
                        tiempoUltimaActualizacion = (Long) inTiempo.readObject();
                    else{
                        tiempoUltimaActualizacion = this.getTiempoUltModif();
                    }
                    //System.out.println("me mandaron el tiempo");
                    inTiempo.close();
                    socket.close();

                    //                    System.out.println("mi tiempo de ultmodif es:"+this.getTiempoUltModif());
                    //                    System.out.println("el del server es:" +tiempoUltimaActualizacion);
                    if (this.getTiempoUltModif() < tiempoUltimaActualizacion) {
                        //System.out.println("ENTRE");
                        Socket socketDest = new Socket();
                        InetSocketAddress addr2 =
                            new InetSocketAddress(IPDirectorioActual, this.puertoDestActual);
                        socketDest.connect(addr2, 500);
                        ObjectInputStream inDest = new ObjectInputStream(socketDest.getInputStream());

                        Collection<IDatosReceptor> destinatariosRegistrados;
                        //System.out.println("esperando  los receptores");
                        destinatariosRegistrados = (Collection<IDatosReceptor>) inDest.readObject();
                        //System.out.println("me llegaron son " + destinatariosRegistrados.toString());
                        //                        System.out.println("EL CONROLADOR EMISOR ES: "+ControladorEmisor.getInstance());
                        //                        System.out.println("LA AGENDA ES :"+destinatariosRegistrados);
                        sistemaEmisor.setAgenda(destinatariosRegistrados);

                        //for(Iterator<Receptor> it = destinatariosRegistrados.iterator(); it.hasNext(); ){
                        //    System.out.println(it.next().descripcionCompleta());
                        //}

                        this.tiempoUltModif = tiempoUltimaActualizacion;
                        inDest.close();
                        socketDest.close();
                    } 

                    sistemaEmisor.updateConectado(true);
                    Thread.sleep(TIEMPO_ACTUALIZACION_DESTINATARIOS); //no lo actualiza siempre xq es lindo
                }


            } catch (Exception e) {
//                e.printStackTrace();
                this.cambiarDirectorioActivo();
                sistemaEmisor.updateConectado(false);
            }
        }

    }
    
    private void cambiarDirectorioActivo(){
        if(this.usandoDirSecundario){
            this.IPDirectorioActual = this.IPDirectorio;
            this.puertoDestActual = this.puertoDirectorioDestinatarios;
            this.puertoTiempoActual = this.puertoDirectorioTiempo;
            
        }
        else {
            this.IPDirectorioActual = this.ipDirectorioSecundario;
            this.puertoDestActual = this.puertoDirectorioSecundarioDest;
            this.puertoTiempoActual  = this.puertoDirectorioSecundarioTiempo;
        }
        this.usandoDirSecundario = !this.usandoDirSecundario;
    }
    
}
