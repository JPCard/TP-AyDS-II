package directorio.red;

import directorio.modelo.Directorio;

import directorio.modelo.DirectorioMain;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;
import java.io.OutputStream;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.net.SocketException;

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
            try (ServerSocket s = new ServerSocket(HEARTBEAT_PORT)) {
                while (true) {
                    System.out.println("Hilo Heartbeats: Esperando un heartbeat...");
                    Heartbeat heartbeat;
                    try (Socket socket = s.accept()) {
                        System.out.println("Hilo Heartbeats: heartbeat recibido");
                        //aca llega un heartbeat
                        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                            heartbeat = (Heartbeat) in.readObject();
                            System.err.println(heartbeat.toString());
                            
                            
                            
                            if (!heartbeat.isRetransmitido()) { //avisar a los otros directorios
                                heartbeat.setRetransmitido(true);
                                new Thread(new HeartbeatRetransmitirThread(heartbeat)).start();

                            }

                            new Thread(new HeartbeatHandler(heartbeat)).start();
                            
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


}
