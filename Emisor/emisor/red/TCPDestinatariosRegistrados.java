package emisor.red;

import emisor.controlador.ControladorEmisor;

import emisor.modelo.SistemaEmisor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.Collection;

import receptor.modelo.Receptor;
import receptor.modelo.SistemaReceptor;

import receptor.red.TCPHeartbeat;

public class TCPDestinatariosRegistrados implements Runnable {
    private String IPDirectorio;
    private int PuertoDirectorio;
    public static final int TIEMPO_ACTUALIZACION_DESTINATARIOS = 500; // en MS
    
    
    public TCPDestinatariosRegistrados(String IPDirectorio,int PuertoDirectorio) {
        super();
        this.IPDirectorio = IPDirectorio;
        this.PuertoDirectorio = PuertoDirectorio;
    }

    @Override
    public void run() {
        try {

            
            while (true) {
                Socket socket = new Socket();
                InetSocketAddress addr = new InetSocketAddress(IPDirectorio, this.PuertoDirectorio);
                socket.connect(addr,500);
                
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Collection<Receptor> destinatariosRegistrados;
                destinatariosRegistrados = (Collection<Receptor>) in.readObject();
                
                ControladorEmisor.getInstance().setAgenda(destinatariosRegistrados);
                
                in.close();
                socket.close();
                Thread.sleep(TIEMPO_ACTUALIZACION_DESTINATARIOS);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
