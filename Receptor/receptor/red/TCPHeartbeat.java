package receptor.red;

import directorio.modelo.Directorio;



import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;

import java.net.InetSocketAddress;
import java.net.Socket;


import receptor.controlador.ControladorReceptor;

import receptor.modelo.Heartbeat;
import receptor.modelo.Receptor;
import receptor.modelo.SistemaReceptor;

public class TCPHeartbeat implements Runnable {
    private String IPDirectorio;
    private int puertoHeartbeat;
    public static final int TIEMPO_HEARTBEAT = 500; // en MS
    

    private Receptor receptor = SistemaReceptor.getInstance().getReceptor();

    public TCPHeartbeat(String IPDirectorio, int puertoHeartbeat) {
        super();
        this.IPDirectorio = IPDirectorio;
        this.puertoHeartbeat = puertoHeartbeat;
    }

    @Override
    public void run() {

        while (true) {
            try {

                while (true) {
                    Receptor receptor = SistemaReceptor.getInstance().getReceptor();
                    Heartbeat heartbeat = new Heartbeat(receptor);
                    
                    Socket socket = new Socket();
                    InetSocketAddress addr = new InetSocketAddress(IPDirectorio, this.puertoHeartbeat);
                    socket.connect(addr, 500);

                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeObject(heartbeat);
                    out.close();
                    
                    socket.close();
                    ControladorReceptor.getInstance().updateConectado(true);
                    Thread.sleep(TIEMPO_HEARTBEAT);
                }


            } catch (Exception e) {
                //e.printStackTrace();
                ControladorReceptor.getInstance().updateConectado(false);
            }
        }

    }
}
