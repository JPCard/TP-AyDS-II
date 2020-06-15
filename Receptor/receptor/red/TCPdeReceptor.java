package receptor.red;

import emisor.modelo.Emisor;

import emisor.modelo.IMensaje;

import java.io.IOException;
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
            
        while(true){
            try (ServerSocket s = new ServerSocket(SistemaReceptor.getInstance().getPuerto());) {
                     
    
                    while (true) {
                        try(Socket socket = s.accept()){
                            
                            try(ObjectInputStream in = new ObjectInputStream(socket.getInputStream())){
                        
                        IMensaje mensaje = (IMensaje) in.readObject();
                        
                        ControladorReceptor.getInstance().mostrarMensaje(mensaje);
                        
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                        }
                    }
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }

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
