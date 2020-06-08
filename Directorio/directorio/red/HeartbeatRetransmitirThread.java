package directorio.red;

import directorio.modelo.Directorio;

import java.io.IOException;
import java.io.ObjectOutputStream;

import java.net.InetSocketAddress;
import java.net.Socket;

import receptor.modelo.Heartbeat;

public class HeartbeatRetransmitirThread implements Runnable {
    private Heartbeat heartbeat;


    public HeartbeatRetransmitirThread(Heartbeat heartbeat) {
        super();
        this.heartbeat = heartbeat;
    }

    @Override
    public void run() {
        try {
            Directorio.getInstance().getIpOtroDirectorio();

            Socket socketRetransmitir = new Socket();
            InetSocketAddress addr =
                new InetSocketAddress(Directorio.getInstance().getIpOtroDirectorio(),
                                      Directorio.getInstance().getOtroDirectorioPuertoHeartbeats());

            socketRetransmitir.connect(addr, 500);


            ObjectOutputStream out = new ObjectOutputStream(socketRetransmitir.getOutputStream());
            out.writeObject(heartbeat);
            out.close();

            socketRetransmitir.close();
        } catch (IOException e) {
        }
    }
}
