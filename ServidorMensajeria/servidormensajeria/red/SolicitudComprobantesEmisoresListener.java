package servidormensajeria.red;

import emisor.modelo.Emisor;
import emisor.modelo.Mensaje;

import emisor.modelo.MensajeConComprobante;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Collection;

import receptor.modelo.Comprobante;

import servidormensajeria.modelo.SistemaServidor;

public class SolicitudComprobantesEmisoresListener implements Runnable{
    private ServerSocket s;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    public SolicitudComprobantesEmisoresListener() {
        super();
    }

    @Override
    public void run(){
            
        while(true){
            try {
                    s = new ServerSocket(SistemaServidor.getInstance().cargarPuertoDevolverMensajesEmisores());
                    while (true) {
                        System.out.println("Servicio de recuperacion de comprobantes para emisores: esperando...");
                         socket = s.accept();
                        
                         in = new ObjectInputStream(socket.getInputStream());
                        Emisor emisor = (Emisor) in.readObject();
                        System.out.println(emisor.getNombre()+" acaba de solicitar comprobantes de cuando no estuvo");
                         out = new ObjectOutputStream(socket.getOutputStream());
                        Collection<Comprobante> enviable = SistemaServidor.getInstance().getPersistencia().getComprobantesNoEnviados(emisor);
                        out.writeObject(enviable);//envio al emisor la id con la cual debe rotular su mensaje
                        out.close();
                        in.close();
                        socket.close();
                        SistemaServidor.getInstance().eliminarComprobantesNoEnviados(emisor);
                    }
            }
            catch (BindException e) { //IP y puerto ya estaban utilizados
                //System.exit(1);
            }
            catch (Exception e) {
                System.out.println("es em smelistener");
                System.err.println("Capturada EOFException");
                try {
                    if (in != null){
                        in.close();
                    }
                    if (socket != null)
                        socket.close();
                    if (s != null)
                        s.close();
                    if (out != null)
                        out.close();

                } catch (IOException f) {
                    f.printStackTrace();
                    System.err.println("esto si es malo");
                }
            }
        }
    }
}