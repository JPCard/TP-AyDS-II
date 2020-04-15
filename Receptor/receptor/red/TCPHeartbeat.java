package receptor.red;

import directorio.modelo.Directorio;



import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;

import java.net.InetSocketAddress;
import java.net.Socket;


import receptor.controlador.ControladorReceptor;

import receptor.modelo.Receptor;
import receptor.modelo.SistemaReceptor;

public class TCPHeartbeat implements Runnable {
    private String IPDirectorio;
    private int puertoHeartbeat;
    private int puertoRegistro;
    public static final int TIEMPO_HEARTBEAT = 500; // en MS
    

    private Receptor receptor = SistemaReceptor.getInstance().getReceptor();

    public TCPHeartbeat(String IPDirectorio, int puertoHeartbeat, int puertoRegistro) {
        super();
        this.IPDirectorio = IPDirectorio;
        this.puertoHeartbeat = puertoHeartbeat;
        this.puertoRegistro = puertoRegistro;
    }

    @Override
    public void run() {

        while (true) {
            try {
                if(receptor.getID() == Directorio.INVALID_ID){ //el directorio avisa al receptor cual es su ID si es que no lo tenia 
                    Socket socketRegistro = new Socket();
                    InetSocketAddress addrRegistro = new InetSocketAddress(IPDirectorio, puertoRegistro);
                    socketRegistro.connect(addrRegistro,456);
                    ObjectInputStream in = new ObjectInputStream(socketRegistro.getInputStream());
                    Integer idReceptorDeDir;
                    idReceptorDeDir = (Integer) in.readObject();
                    SistemaReceptor.getInstance().getReceptor().setID(idReceptorDeDir);
                    in.close(); 
                    socketRegistro.close();
                }

                while (true) {
                    Receptor receptor = SistemaReceptor.getInstance().getReceptor();
                    Socket socket = new Socket();
                    InetSocketAddress addr = new InetSocketAddress(IPDirectorio, this.puertoHeartbeat);
                    socket.connect(addr, 500);

                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeObject(receptor);
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
