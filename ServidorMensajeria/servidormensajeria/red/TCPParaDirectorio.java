package servidormensajeria.red;

import emisor.controlador.ControladorEmisor;

import emisor.modelo.Mensaje;

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

import servidormensajeria.red.MensajeHandler;

import servidormensajeria.modelo.SistemaServidor;

public class TCPParaDirectorio implements Runnable {

    private String ipDirectorio;
    private int puertoDirectorioTiempo;
    private int puertoDirectorioDestinatarios;


    public TCPParaDirectorio(String ipDirectorio, int puertoDirectorioDestinatarios, int puertoDirectorioTiempo) {
        this.ipDirectorio = ipDirectorio;
        this.puertoDirectorioTiempo = puertoDirectorioTiempo;
        this.puertoDirectorioDestinatarios = puertoDirectorioDestinatarios;
        envioInicialMensajesAsincronicos();
    }


    /**
     * Le pide un receptor al directorio para que se traiga la lista de receptores completa
     * y le envia mensajes asincronicos pendientes a cada receptor conectado
     */
    public void envioInicialMensajesAsincronicos() {
        this.getReceptor("");
        for (Receptor receptor : SistemaServidor.getInstance().getReceptores()) {
            try {
                if (receptor.isConectado())
                    this.envioMensajesAsincronicos(receptor);
            } catch (Exception e) {
            }
        }
    }

    /**
     * @param usuarioActual
     * @return null si el receptor no esta conectado o no lo encontró, != null si el receptor esta conectado
     */


    public Receptor getReceptor(String usuarioActual) {
        try {
            Socket socketTiempo = new Socket();
            InetSocketAddress addrTiempo = new InetSocketAddress(this.ipDirectorio, this.puertoDirectorioTiempo);

            socketTiempo.connect(addrTiempo, 500);
            ObjectInputStream in;

            in = new ObjectInputStream(socketTiempo.getInputStream());


            Long tiempoUltimaActualizacion = (Long) in.readObject();
            in.close();
            socketTiempo.close();

            if (SistemaServidor.getInstance().getTiempoUltimaActualizacionReceptores() < tiempoUltimaActualizacion) {


                Socket socketDest = new Socket();
                InetSocketAddress addr2 = new InetSocketAddress(ipDirectorio, this.puertoDirectorioDestinatarios);
                socketDest.connect(addr2, 500);
                ObjectInputStream inDest = new ObjectInputStream(socketDest.getInputStream());

                SistemaServidor.getInstance().setReceptores((ArrayList<Receptor>) inDest.readObject());


                inDest.close();
                socketDest.close();


                SistemaServidor.getInstance().setTiempoUltimaActualizacionReceptores(tiempoUltimaActualizacion);

            }


            ArrayList<Receptor> receptoresArray = SistemaServidor.getInstance().getReceptores();
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

        } catch (IOException e) {
            //e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // e.printStackTrace();
        }
        System.err.println("estoy mandando nullafangos");
        return null;

    }

    @Override
    public void run() {
        while (true) {
            try (ServerSocket s = new ServerSocket(SistemaServidor.getInstance().cargarPuertoInfoDirectorio())) {


                while (true) {
                    System.out.println("Hilo notifica sistema de mensajes: esperando");
                    try (Socket socket = s.accept()) {
                        System.out.println("Nuevo receptor: toca enviarle mensajes q le faltan");

                        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                            Receptor receptor = (Receptor) in.readObject();
                            envioMensajesAsincronicos(receptor);
                        } catch (Exception e) {
                            System.out.println("mmm!");
                            e.printStackTrace();
                        }
                    }


                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void envioMensajesAsincronicos(Receptor receptor) throws Exception {
        Collection<Mensaje> mensajes = SistemaServidor.getInstance().obtenerMsjsPendientesReceptor(receptor);
        for (Mensaje mensaje : mensajes) {
            new Thread(new MensajeHandler(mensaje, false)).start();
        }
    }

}
