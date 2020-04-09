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
                        int idAnt = receptor.getID();
                        directorio.heartbeatRecibido(receptor);
                        in.close();
                        
                        if(idAnt == Directorio.INVALID_ID){ //avisa al receptor cual es su ID si es que no lo tenia 
                            InetSocketAddress addr = new InetSocketAddress(receptor.getIP(), DirectorioMain.PUERTO_RECIBIR_ID_RECEPTOR);
                            Socket socketMandarID = new Socket();
                            socketMandarID.connect(addr,250);
                            ObjectOutputStream out = new ObjectOutputStream(socketMandarID.getOutputStream());
                            out.writeObject(receptor.getID());
                            out.close();
                        }
                    }
                    


                    socket.close();
                }

            } catch (BindException e) { //IP y puerto ya estaban utilizados
                //System.exit(1); no lo dejamos cerrar
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
