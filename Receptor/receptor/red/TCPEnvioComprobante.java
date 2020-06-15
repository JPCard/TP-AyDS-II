package receptor.red;

import emisor.modelo.IDatosEmisor;

import emisor.modelo.IDatosEmisor;
import emisor.modelo.IMensaje;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;


import receptor.controlador.ControladorReceptor;

import receptor.modelo.IComprobante;
import receptor.modelo.SistemaReceptor;

public class TCPEnvioComprobante implements IEnvioComprobante {
    
    private String ipServidorMensajeria;
    private int puertoServidorMensajeria;
    
    public TCPEnvioComprobante(String ipServidorMensajeria, int puertoServidorMensajeria) {
        this.ipServidorMensajeria = ipServidorMensajeria;
        this.puertoServidorMensajeria = puertoServidorMensajeria;
    }

    
   
    public void enviarComprobante(IComprobante comprobante, IDatosEmisor emisor){
        
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
