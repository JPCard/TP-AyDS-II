package receptor.red;

import emisor.modelo.Mensaje;

import emisor.modelo.SistemaEmisor;

import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Iterator;

import receptor.controlador.ControladorReceptor;

import receptor.modelo.Receptor;
import receptor.modelo.SistemaReceptor;

public class TCPHeartbeat implements Runnable {
    private String IPDirectorio;
    private int PuertoDirectorio;
    public static final int TIEMPO_HEARTBEAT = 500; // en MS
    
    private Receptor receptor = SistemaReceptor.getInstance().getReceptor();
    
    public TCPHeartbeat(String IPDirectorio,int PuertoDirectorio) {
        super();
        this.IPDirectorio = IPDirectorio;
        this.PuertoDirectorio = PuertoDirectorio;
    }

    @Override
    public void run(){
            
        
            try {


                while (true) {
                    Socket socket = new Socket();
                    InetSocketAddress addr = new InetSocketAddress(IPDirectorio, this.PuertoDirectorio);
                    socket.connect(addr,500);
                    
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    //out.writeObject(SistemaReceptor.getInstance().getReceptor()); //si algun dato cambia y hay que mandar un receptor distinto
                    out.writeObject(receptor);
                    out.close();
                    socket.close();
                    wait(TIEMPO_HEARTBEAT);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
