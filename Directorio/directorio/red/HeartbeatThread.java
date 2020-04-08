package directorio.red;

import directorio.modelo.Directorio;

import java.io.ObjectInputStream;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import java.security.AccessControlContext;

import java.util.GregorianCalendar;

import receptor.modelo.Receptor;

public class HeartbeatThread extends Thread {
    private Directorio directorio;
    private final int HEARTBEAT_PORT;


    public HeartbeatThread(Directorio directorio, int HEARTBEAT_PORT) {
        super();
        this.directorio = directorio;
        this.HEARTBEAT_PORT = HEARTBEAT_PORT;
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
                    Socket socket = s.accept();
                    System.out.println("Hilo Heartbeats: heartbeat recibido");
                    ObjectInputStream in = null;
                    //aca llega un heartbeat
                    if (socket.isConnected()) {
                        in = new ObjectInputStream(socket.getInputStream());
                        Receptor receptor = (Receptor) in.readObject();
                        directorio.heartbeatRecibido(receptor);
                        in.close();
                    }


                    socket.close();
                }

            } catch (BindException e) { //IP y puerto ya estaban utilizados
                System.exit(1);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
