package servidormensajeria.red;

import emisor.modelo.Mensaje;

import emisor.modelo.MensajeConComprobante;

import java.io.ObjectInputStream;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import receptor.modelo.Comprobante;

import servidormensajeria.modelo.SistemaServidor;

public class ComprobanteListener implements Runnable{
    public ComprobanteListener() {
        super();
        //TODO
    }

    @Override
    public void run(){
            try {
                    ServerSocket s = new ServerSocket(SistemaServidor.PUERTO_COMPROBANTES);
                    while (true) {
                        System.out.println("toy esperando un comprobante");
                        Socket socket = s.accept();
                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                        Comprobante comprobante = (Comprobante) in.readObject();
                        SistemaServidor.getInstance().arriboComprobante(comprobante);
                        in.close();
                        socket.close();
                        System.out.println("RECIBI UN comprobante");
                        System.out.println("dice que:");
                        System.out.println(comprobante.getReceptor());
                        System.out.println(comprobante.getidMensaje());
                        
                    }
            }
            catch (BindException e) { //IP y puerto ya estaban utilizados
                System.exit(1);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
}
