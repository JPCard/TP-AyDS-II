package receptor.red;

import emisor.modelo.IMensaje;

import java.io.IOException;
import java.io.ObjectInputStream;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import receptor.controlador.ControladorReceptor;

import receptor.modelo.ISistemaReceptor;
import receptor.modelo.SistemaReceptor;

public class TCPMensajeListener implements IMensajeListener {
    private ISistemaReceptor sistemaReceptor;

    public TCPMensajeListener(ISistemaReceptor sistemaReceptor) {
        this.sistemaReceptor = sistemaReceptor;
    }

    @Override
    public void run(){
            
        while(true){
            try (ServerSocket s = new ServerSocket(sistemaReceptor.getPuerto());) {
                     
    
                    while (true) {
                        try(Socket socket = s.accept()){
                            
                            try(ObjectInputStream in = new ObjectInputStream(socket.getInputStream())){
                        
                        IMensaje mensaje = (IMensaje) in.readObject();
                        
                        sistemaReceptor.arriboMensaje(mensaje);
                        
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                        }
                    }
                    }
            }catch(BindException f){
                System.err.println("Puerto utilizado, cerrando");
                //f.printStackTrace();
                System.exit(1);// puerto ya en uso, se cierra.
            }
            
            catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    
}
