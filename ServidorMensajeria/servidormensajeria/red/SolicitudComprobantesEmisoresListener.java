package servidormensajeria.red;

import emisor.modelo.Emisor;
import emisor.modelo.IMensaje;

import emisor.modelo.MensajeConComprobante;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Collection;

import receptor.modelo.Comprobante;

import servidormensajeria.modelo.SistemaServidor;

public class SolicitudComprobantesEmisoresListener implements Runnable {

    public SolicitudComprobantesEmisoresListener() {
        super();
    }

    @Override
    public void run() {

        while (true) {
            try (ServerSocket s =
                 new ServerSocket(SistemaServidor.getInstance().cargarPuertoDevolverMensajesEmisores())) {

                while (true) {
                    System.out.println("Servicio de recuperacion de comprobantes para emisores: esperando...");
                    try (Socket socket = s.accept()) {

                        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                            Emisor emisor = (Emisor) in.readObject();
                            System.out.println(emisor.getNombre() +
                                               " acaba de solicitar comprobantes de cuando no estuvo");
                            try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
                                Collection<Comprobante> enviable = SistemaServidor.getInstance()
                                                                                  .getPersistencia()
                                                                                  .getComprobantesNoEnviados(emisor);
                                out.writeObject(enviable); //envio al emisor la id con la cual debe rotular su mensaje
                                SistemaServidor.getInstance().eliminarComprobantesNoEnviados(emisor);
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
}
