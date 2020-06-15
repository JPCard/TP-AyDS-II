package directorio.red;

import directorio.modelo.IDirectorio;

import directorio.modelo.IDirectorio;

import java.io.IOException;
import java.io.ObjectOutputStream;

import java.net.InetSocketAddress;
import java.net.Socket;

import receptor.modelo.Heartbeat;

public class HeartbeatRetransmitirThread implements Runnable {
    private Heartbeat heartbeat;
    private IDirectorio directorio;

    public HeartbeatRetransmitirThread(Heartbeat heartbeat) {
        super();
        this.heartbeat = heartbeat;
    }

    @Override
    public void run() {
        try {
            directorio.getIpOtroDirectorio();

            Socket socketRetransmitir = new Socket();
            InetSocketAddress addr =
                new InetSocketAddress(directorio.getIpOtroDirectorio(),
                                      directorio.getOtroDirectorioPuertoHeartbeats());

            socketRetransmitir.connect(addr, 500);


            ObjectOutputStream out = new ObjectOutputStream(socketRetransmitir.getOutputStream());
            out.writeObject(heartbeat);
            out.close();

            socketRetransmitir.close();
        } catch (IOException e) {
        }
    }
}
