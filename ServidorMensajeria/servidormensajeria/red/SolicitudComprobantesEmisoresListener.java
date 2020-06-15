package servidormensajeria.red;

import emisor.modelo.IDatosEmisor;
import emisor.modelo.IMensaje;

import emisor.modelo.MensajeConComprobante;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Collection;

import receptor.modelo.IComprobante;

import servidormensajeria.modelo.ISistemaServidor;
import servidormensajeria.modelo.SistemaServidor;

public class SolicitudComprobantesEmisoresListener implements Runnable {

    private ISistemaServidor sistemaServidor;

    public SolicitudComprobantesEmisoresListener(ISistemaServidor sistemaServidor) {
        this.sistemaServidor = sistemaServidor;
    }

    @Override
    public void run() {

        while (true) {
            try (ServerSocket s =
                 new ServerSocket(sistemaServidor.cargarPuertoDevolverMensajesEmisores())) {

                while (true) {
                    System.out.println("Servicio de recuperacion de comprobantes para emisores: esperando...");
                    try (Socket socket = s.accept()) {

                        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                            IDatosEmisor emisor = (IDatosEmisor) in.readObject();
                            System.out.println(emisor.getNombre() +
                                               " acaba de solicitar comprobantes de cuando no estuvo");
                            try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
                                Collection<IComprobante> enviable = sistemaServidor
                                                                                  .getPersistencia()
                                                                                  .getComprobantesNoEnviados(emisor);
                                out.writeObject(enviable); //envio al emisor la id con la cual debe rotular su mensaje
                                sistemaServidor.eliminarComprobantesNoEnviados(emisor);
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
