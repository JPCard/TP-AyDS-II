package receptor.red;

import emisor.modelo.Emisor;

import emisor.modelo.Mensaje;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.ServerSocket;
import java.net.Socket;


import receptor.controlador.ControladorReceptor;

import receptor.modelo.Comprobante;
import receptor.modelo.SistemaReceptor;

public class TCPdeReceptor  implements Runnable{
    
    public TCPdeReceptor() {
        super();
    }

    
    public void run(){
        try {
                            ServerSocket s = new ServerSocket(SistemaReceptor.getInstance().getPuerto());

                            while (true) {
                                
                                Socket socket = s.accept();
                                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                                
                                Mensaje mensaje = (Mensaje) in.readObject();
                                
                                ControladorReceptor.getInstance().mostrarMensaje(mensaje);
                                
                                in.close();
                                socket.close();
                            }

                        } catch (Exception e) {
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
