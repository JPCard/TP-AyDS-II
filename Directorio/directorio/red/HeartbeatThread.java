package directorio.red;

import directorio.modelo.Directorio;

import directorio.modelo.DirectorioMain;

import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;
import java.io.OutputStream;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.security.AccessControlContext;

import java.util.GregorianCalendar;

import receptor.modelo.Heartbeat;
import receptor.modelo.Receptor;

public class HeartbeatThread extends Thread {
    private Directorio directorio;
    private final int HEARTBEAT_PORT;


    public HeartbeatThread(Directorio directorio) {
        super();
        this.directorio = directorio;
        this.HEARTBEAT_PORT = directorio.getPuertoRecibeHeartbeats();
    }

    @Override
    public void run() {
        super.run();
        this.escucharHeartBeats();
    }


    private void escucharHeartBeats() {
        while (true) {
            try {
                ServerSocket s = new ServerSocket(HEARTBEAT_PORT);

                while (true) {
                    System.out.println("Hilo Heartbeats: Esperando un heartbeat...");
                    Heartbeat heartbeat;
                    Socket socket = s.accept();
                    System.out.println("Hilo Heartbeats: heartbeat recibido");
                    ObjectInputStream in = null;
                    //aca llega un heartbeat
                        in = new ObjectInputStream(socket.getInputStream());
                        heartbeat = (Heartbeat) in.readObject();
                        directorio.heartbeatRecibido(heartbeat.getReceptor());
                        in.close();
                    
                    
                    

                    socket.close();
                    
                    if(!heartbeat.isRetransmitido()){ //avisar a los otros directorios
                        heartbeat.setRetransmitido(true);
                        directorio.getIpOtroDirectorio();
                        
                        Socket socketRetransmitir = new Socket();
                        InetSocketAddress addr = new InetSocketAddress(directorio.getIpOtroDirectorio(),directorio.getOtroDirectorioPuertoHeartbeats());
                        socketRetransmitir.connect(addr, 500);

                        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                        out.writeObject(heartbeat);
                        out.close();
                        
                        socketRetransmitir.close();
                                  
                    }
                    
                    
                }

            } catch (BindException e) { //IP y puerto ya estaban utilizados
                //System.exit(1); no lo dejamos cerrar
            } catch (Exception e) {
                //e.printStackTrace();
            }

        }
    }
}
