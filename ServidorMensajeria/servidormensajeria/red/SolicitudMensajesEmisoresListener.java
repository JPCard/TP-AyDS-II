package servidormensajeria.red;

import emisor.modelo.Emisor;
import emisor.modelo.Mensaje;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import servidormensajeria.modelo.SistemaServidor;

public class SolicitudMensajesEmisoresListener implements Runnable{
    public SolicitudMensajesEmisoresListener() {
        super();
    }

    @Override
    public void run(){
            try {
                    ServerSocket s = new ServerSocket(SistemaServidor.getInstance().cargarPuertoDevolverMensajesEmisores());
                    while (true) {
                        System.out.println("Servicio de recuperacion de mensajes para emisores: esperando...");
                        Socket socket = s.accept();
                        
                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                        Emisor emisor = (Emisor) in.readObject();
                        
                        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                        out.writeObject(SistemaServidor.getInstance().getPersistencia().obtenerMsjsComprobadosEmisor(emisor));//envio al emisor la id con la cual debe rotular su mensaje
                        
                        out.close();
                        in.close();
                        socket.close();
                    }
            }
            catch (BindException e) { //IP y puerto ya estaban utilizados
                System.exit(1);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
}
