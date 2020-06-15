package servidormensajeria.red;

import emisor.modelo.IMensaje;

import emisor.modelo.MensajeConComprobante;

import java.io.IOException;
import java.io.ObjectInputStream;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import receptor.modelo.IComprobante;

import servidormensajeria.modelo.ISistemaServidor;
import servidormensajeria.modelo.SistemaServidor;

public class ComprobanteListener implements Runnable {
    private ISistemaServidor sistemaServidor;

    public ComprobanteListener(ISistemaServidor sistemaServidor) {
        this.sistemaServidor = sistemaServidor;
    }

    @Override
    public void run() {
        while (true) {
            try (ServerSocket s = new ServerSocket(sistemaServidor.cargarPuertoComprobantes())) {

                while (true) {
                    System.out.println("Esperando comprobantes...");
                    try (Socket socket = s.accept()) {
                        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                            IComprobante comprobante = (IComprobante) in.readObject();
                            System.out.println("IComprobante para " + comprobante.getEmisorOriginal().getNombre() +
                                               " de " + comprobante.getUsuarioReceptor() + " recibido");
                            sistemaServidor.arriboComprobante(comprobante);

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
