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

            int indice =
                Collections.binarySearch(receptoresArray, new Receptor("123213", 12312, "AAAAAAAAAA", usuarioActual));
//            System.out.println("INDICE INDICE INDICE " + indice);
            if (indice == -1)
                return null;
            else
                return receptoresArray.get(indice);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public void run() {
        while (true) {
            try {
                ServerSocket s = new ServerSocket(SistemaServidor.PUERTO_INFO_DIRECTORIO); // TODO

                while (true) {
                    System.out.println("Hilo notifica sistema de mensajes: esperando");
                    Socket socket = s.accept();
                    System.out.println("Nuevo receptor: toca enviarle mensajes q le faltan");
                    ObjectInputStream in = null;
                    in = new ObjectInputStream(socket.getInputStream());
                    Receptor receptor = (Receptor) in.readObject();

                    Collection<Mensaje> mensajes =
                        SistemaServidor.getInstance().obtenerMsjsPendientesReceptor(receptor);
                    for (Mensaje m : mensajes) {
                        new Thread(new MensajeHandler(m)).start();//todo banderita arrastrada
                    }


                    in.close();
                    socket.close();
                }

            } catch (BindException e) { //IP y puerto ya estaban utilizados
                //System.exit(1); no lo dejamos cerrar
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
