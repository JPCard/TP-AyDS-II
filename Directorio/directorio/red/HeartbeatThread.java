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

    private Socket socket;
    private ObjectInputStream in;
    private ServerSocket s;


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
                s = new ServerSocket(HEARTBEAT_PORT);

                while (true) {
                    System.out.println("Hilo Heartbeats: Esperando un heartbeat...");
                    Heartbeat heartbeat;
                    socket = s.accept();
                    System.out.println("Hilo Heartbeats: heartbeat recibido");
                    ObjectInputStream in = null;
                    //aca llega un heartbeat
                    in = new ObjectInputStream(socket.getInputStream());
                    heartbeat = (Heartbeat) in.readObject();
                    in.close();
                    socket.close();
                    System.err.println(heartbeat.toString());

                    if (!heartbeat.isRetransmitido()) { //avisar a los otros directorios
                        heartbeat.setRetransmitido(true);
                        new Thread(new HeartbeatRetransmitirThread(heartbeat)).start();

                    }

                    new Thread(new HeartbeatHandler(heartbeat)).start();


                }

            } catch (BindException e) {
                System.out.println("BindException, error fatal");
                System.exit(1);
            }

            catch (Exception e) {//Reconexion de directorios, es posible que queden mal cerrados los sockets de los receptores al perder conexion con el directorio
                System.err.println("Capturada EOFException");
                try {
                    if (in != null)
                        in.close();
                    if (socket != null)
                        socket.close();
                    if (s != null)
                        s.close();

                } catch (IOException f) {f.printStackTrace();
                    System.err.println("esto si es malo");
                }
            }


        }
    }
}
