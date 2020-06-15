package servidormensajeria.red;

import emisor.modelo.IDatosEmisor;
import emisor.modelo.IDatosEmisor;
import emisor.modelo.IMensaje;
import emisor.modelo.IMensaje;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Collection;

import java.util.Iterator;

import receptor.modelo.IComprobante;
import receptor.modelo.SistemaReceptor;

import servidormensajeria.modelo.SistemaServidor;

public class MensajeListener implements Runnable {

    public MensajeListener() {
        super();
    }

    @Override
    public void run() {
        while (true) {
            try (ServerSocket s = new ServerSocket(SistemaServidor.getInstance().cargarPuertoRecepcionMensajes())) {
                while (true) {
                    System.out.println("Sistema Servidor de mensajeria: Esperando Mensajes...");
                    try (Socket socket = s.accept()) {

                        //IN 1
                        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                            int cantMensajes = (Integer) in.readObject();

                            //OUT 1
                            try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
                                for (int i = 0; i < cantMensajes; i++) {
                                    Integer nuevaid = SistemaServidor.getInstance()
                                                                     .getPersistencia()
                                                                     .getProximoIdMensaje();
                                    out.writeObject(nuevaid); //envio al emisor la id con la cual debe rotular su mensaje
                                    SistemaServidor.getInstance()
                                                   .getPersistencia()
                                                   .avanzaProximoIdMensaje();
                                }


                                //IN 2
                                Collection<IMensaje> mensajes = (Collection<IMensaje>) in.readObject();
                                for (Iterator<IMensaje> it = mensajes.iterator(); it.hasNext();) {
                                    SistemaServidor.getInstance().arriboMensaje(it.next());
                                }

                                IMensaje primerMensaje = mensajes.iterator().next(); //TODO borrar es de debug
                                System.out.println("Sistema Servidor de mensajeria: IMensaje de " +
                                                   primerMensaje.getEmisor().getNombre() + " recibido");
                                //TODO borrar
                                System.out.println("Dice que asunto: " + primerMensaje.getAsunto());
                                System.out.println("Y ademas que cuerpo: " + primerMensaje.getCuerpo());
                            }
                        } catch (ClassNotFoundException e) {
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

    public void enviarComprobante(IComprobante comprobante, IDatosEmisor emisor) {

        try {
            Socket socket = new Socket(emisor.getIP(), emisor.getPuerto());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(comprobante);
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
