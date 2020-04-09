package receptor.red;

import directorio.modelo.Directorio;

import directorio.modelo.DirectorioMain;

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

    public TCPHeartbeat(String IPDirectorio, int PuertoDirectorio) {
        super();
        this.IPDirectorio = IPDirectorio;
        this.PuertoDirectorio = PuertoDirectorio;
    }

    @Override
    public void run() {

        while (true) {
            try {
                if(receptor.getID() == Directorio.INVALID_ID){ //el directorio avisa al receptor cual es su ID si es que no lo tenia 
                    Socket socketRegistro = new Socket();
                    InetSocketAddress addrRegistro = new InetSocketAddress(IPDirectorio, DirectorioMain.REGISTRO_PORT);
                    socketRegistro.connect(addrRegistro,456);
                    ObjectInputStream in = new ObjectInputStream(socketRegistro.getInputStream());
                    Integer idReceptorDeDir;
                    idReceptorDeDir = (Integer) in.readObject();
                    SistemaReceptor.getInstance().getReceptor().setID(idReceptorDeDir);
                    in.close(); 
                    socketRegistro.close();
                }

                while (true) {
                    Receptor receptor = SistemaReceptor.getInstance().getReceptor(); //MIRAR ESTO MIRAR ESTOMIRAR ESTOMIRAR ESTOMIRAR ESTOMIRAR ESTOMIRAR ESTOMIRAR ESTOMIRAR ESTOMIRAR ESTOMIRAR ESTO
                    System.out.println("MI ID eS "+SistemaReceptor.getInstance().getReceptor().getID());
                    Socket socket = new Socket();
                    InetSocketAddress addr = new InetSocketAddress(IPDirectorio, this.PuertoDirectorio);
                    socket.connect(addr, 500);

                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    //out.writeObject(SistemaReceptor.getInstance().getReceptor()); //si algun dato cambia y hay que mandar un receptor distinto
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
