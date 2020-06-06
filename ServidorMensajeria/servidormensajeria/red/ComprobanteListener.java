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
    }

    @Override
    public void run(){
        while(true){
            try {
                    ServerSocket s = new ServerSocket(SistemaServidor.getInstance().cargarPuertoComprobantes());
                    while (true) {
                        System.out.println("Esperando comprobantes...");
                        Socket socket = s.accept();
                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                        Comprobante comprobante = (Comprobante) in.readObject();
                        SistemaServidor.getInstance().arriboComprobante(comprobante);
                        in.close();
                        socket.close();
                        System.out.println("Comprobante para "+comprobante.getEmisorOriginal().getNombre()+" de "+comprobante.getUsuarioReceptor()+" recibido");
                        
                    }
            }
            catch (BindException e) { //IP y puerto ya estaban utilizados
                //System.exit(1);
            }
            catch (Exception e) {
                System.out.println("es en comprobantelistener");
               e.printStackTrace();
            }
        }
    }
}
