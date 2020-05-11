package emisor.red;

import emisor.controlador.ControladorEmisor;

import emisor.modelo.SistemaEmisor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.ArrayList;
import java.util.Collection;

import receptor.modelo.Receptor;

import servidormensajeria.modelo.SistemaServidor;

public class TCPDestinatariosRegistrados implements Runnable {
    private String IPDirectorio;
    private int PuertoDirectorio;
    public static final int TIEMPO_ACTUALIZACION_DESTINATARIOS = 1000;// en MS
    private Long tiempoUltModif = new Long(-999); 


    public TCPDestinatariosRegistrados(String IPDirectorio, int PuertoDirectorio) {
        super();
        this.IPDirectorio = IPDirectorio;
        this.PuertoDirectorio = PuertoDirectorio;
    }

    @Override
    public void run() {
        while (true) {
            try {


                while (true) {
                    Socket socket = new Socket();
                    InetSocketAddress addr = new InetSocketAddress(IPDirectorio, this.PuertoDirectorio);
                    socket.connect(addr, 500);

                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    
                    
                    Long tiempoUltimaActualizacion = (Long) in.readObject();
                    if (this.tiempoUltModif < tiempoUltimaActualizacion) {
                        
                        
                        Collection<Receptor> destinatariosRegistrados;
                        destinatariosRegistrados = (Collection<Receptor>) in.readObject();

                        ControladorEmisor.getInstance().setAgenda(destinatariosRegistrados);
                        
                        
                        this.tiempoUltModif = tiempoUltimaActualizacion;
                    }
                    in.close();
                    socket.close();
                    ControladorEmisor.getInstance().updateConectado(true);
                    Thread.sleep(TIEMPO_ACTUALIZACION_DESTINATARIOS);//no lo actualiza siempre xq es lindo
                }


            } catch (Exception e) {
                e.printStackTrace();
                ControladorEmisor.getInstance().updateConectado(false);
            }
        }

    }
}
