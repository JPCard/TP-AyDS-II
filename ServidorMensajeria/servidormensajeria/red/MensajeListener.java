package servidormensajeria.red;

import emisor.modelo.Emisor;
import emisor.modelo.Mensaje;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import receptor.modelo.Comprobante;
import receptor.modelo.SistemaReceptor;

import servidormensajeria.modelo.SistemaServidor;

public class MensajeListener implements Runnable{
    public MensajeListener() {
        super();
    }
    
    @Override
    public void run(){
            try {
                    ServerSocket s = new ServerSocket(SistemaServidor.PUERTO_MENSAJES);
                    while (true) {
                        System.out.println("toy esperando");
                        Socket socket = s.accept();
                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                        Mensaje mensaje = (Mensaje) in.readObject();
                        SistemaServidor.getInstance().arriboMensaje(mensaje);
                        in.close();
                        socket.close();
                        System.out.println("RECIBI UN MENSAJE");
                        System.out.println("dice que:");
                        System.out.println(mensaje.getAsunto());
                        System.out.println(mensaje.getCuerpo());
                        System.out.println(mensaje.getEmisor());
                        System.out.println(mensaje.getId());
                        System.out.println(mensaje.getReceptores().next());
                    }
            }
            catch (BindException e) { //IP y puerto ya estaban utilizados
                System.exit(1);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    
    public void enviarComprobante(Comprobante comprobante,Emisor emisor){
        
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