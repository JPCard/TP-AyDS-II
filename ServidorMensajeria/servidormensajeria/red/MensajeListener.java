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
                    ServerSocket s = new ServerSocket(SistemaServidor.getInstance().cargarPuertoRecepcionMensajes());
                    while (true) {
                        System.out.println("Sistema Servidor de mensajeria: Esperando Mensajes...");
                        Socket socket = s.accept();
                        
                        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                        
                        Integer nuevaid = SistemaServidor.getInstance().getPersistencia().getProximoIdMensaje();
                        
                        
                        out.writeObject(nuevaid);//envio al emisor la id con la cual debe rotular su mensaje
                        SistemaServidor.getInstance().getPersistencia().avanzaProximoIdMensaje(); //solo avanza cuando mando la id
            
                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                        Mensaje mensaje = (Mensaje) in.readObject();
                        SistemaServidor.getInstance().arriboMensaje(mensaje);
                        out.close();
                        in.close();
                        socket.close();
                        System.out.println("Sistema Servidor de mensajeria: Mensaje de "+mensaje.getEmisor().getNombre()+" recibido");
                    }
            }
            catch (BindException e) { //IP y puerto ya estaban utilizados
                System.exit(1);
            }
            catch (Exception e) {
               // e.printStackTrace();
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
           // e.printStackTrace();
        }
        
    }
    
}
