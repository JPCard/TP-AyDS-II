package emisor.red;

import emisor.controlador.ControladorEmisor;

import emisor.modelo.Emisor;
import emisor.modelo.Mensaje;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.ServerSocket;
import java.net.Socket;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

public class EmisionTCP implements Runnable{
    private static EmisionTCP instance = null;
    
    private EmisionTCP() {
        super();
    }


    
    public static EmisionTCP getInstance(){
        if(instance == null)
            instance = new EmisionTCP();
        return instance;
    }
    
    /**
     * Precondicion: El objeto que llega en XML es siempre un comprobante
     */
    public void run(){
        try {
                            ServerSocket s = new ServerSocket(Integer.parseInt(Emisor.getInstance().getPuerto()));

                            while (true) {
                                Socket soc = s.accept();
                                
                                XMLDecoder xmlDecoder = new XMLDecoder(soc.getInputStream());
                                

                                Comprobante comprobante = (Comprobante) xmlDecoder.readObject();
                                this.onConfirmacion(comprobante);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
    }
    
    public void enviarMensaje(Mensaje mensaje, Receptor receptor){
        
        
        try {
            Socket socket = new Socket(receptor.getIP(),receptor.getPuerto());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            XMLEncoder xmlEncoder = new XMLEncoder(socket.getOutputStream());
            xmlEncoder.writeObject(mensaje);
            xmlEncoder.close();
            socket.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void onConfirmacion(Comprobante comprobante){
        ControladorEmisor.agregarComprobante(comprobante);
    }
    
}
