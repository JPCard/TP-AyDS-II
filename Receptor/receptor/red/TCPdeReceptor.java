package receptor.red;

import emisor.modelo.Emisor;

import emisor.modelo.Mensaje;

import emisor.red.TCPdeEmisor;

import java.beans.XMLDecoder;

import java.beans.XMLEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.ServerSocket;
import java.net.Socket;

import receptor.controlador.ControladorReceptor;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;
import receptor.modelo.SistemaReceptor;

public class TCPdeReceptor  implements Runnable{
    
    public TCPdeReceptor() {
        super();
    }

    
    public void run(){
        try {
                            ServerSocket s = new ServerSocket(SistemaReceptor.getInstance().getPuerto());

                            while (true) {
                                Socket soc = s.accept();
                                
                                XMLDecoder xmlDecoder = new XMLDecoder(soc.getInputStream());
                                

                                Mensaje mensaje = (Mensaje) xmlDecoder.readObject();
                                ControladorReceptor.getInstance().mostrarMensaje(mensaje);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
    }
    
    public void enviarComprobante(Comprobante comprobante,Emisor emisor){
        
        try {
            Socket socket = new Socket(emisor.getIP(),emisor.getPuerto());
            
            XMLEncoder xmlEncoder = new XMLEncoder(socket.getOutputStream());
            xmlEncoder.writeObject(comprobante);
            xmlEncoder.close();
            socket.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
