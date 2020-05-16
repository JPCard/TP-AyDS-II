package receptor.red;

import emisor.modelo.Emisor;

import emisor.modelo.Mensaje;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;


import receptor.controlador.ControladorReceptor;

import receptor.modelo.Comprobante;
import receptor.modelo.SistemaReceptor;

public class TCPdeReceptor  implements Runnable{
    
    private String ipServidorMensajeria;
    private int puertoServidorMensajeria;
    
    public TCPdeReceptor(String ipServidorMensajeria, int puertoServidorMensajeria) {
        this.ipServidorMensajeria = ipServidorMensajeria;
        this.puertoServidorMensajeria = puertoServidorMensajeria;
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
            }
            catch (BindException e) { //IP y puerto ya estaban utilizados
                System.out.println("Receptor: Puerto ocupado, cerrando.");
            }
            catch (Exception e) {
            }
        }
    
    public void enviarComprobante(Comprobante comprobante,Emisor emisor){
        
        try {
                Socket socket = new Socket(this.ipServidorMensajeria, this.puertoServidorMensajeria);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(comprobante);
                out.close();
                socket.close();
            
        } catch (Exception e) {
           // e.printStackTrace();
        }
        
    }
}
