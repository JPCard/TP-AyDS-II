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
    private int puertoDirectorioTiempo;
    private int puertoDirectorioDestinatarios;
    public static final int TIEMPO_ACTUALIZACION_DESTINATARIOS = 1000;// en MS
    private Long tiempoUltModif = new Long(-999); 


    public TCPDestinatariosRegistrados(String IPDirectorio, int puertoDirectorioTiempo, int puertoDirectorioDestinatarios) {
        super();
        this.IPDirectorio = IPDirectorio;
        this.puertoDirectorioTiempo = puertoDirectorioTiempo;
        this.puertoDirectorioDestinatarios = puertoDirectorioDestinatarios;
    }

    @Override
    public void run() {
        while (true) {
            try {


                while (true) {
                    Socket socket = new Socket();
                    InetSocketAddress addr = new InetSocketAddress(IPDirectorio, this.puertoDirectorioTiempo);
                    socket.connect(addr, 500);
                    ObjectInputStream inTiempo = new ObjectInputStream(socket.getInputStream());
                    Long tiempoUltimaActualizacion = (Long) inTiempo.readObject();
                    inTiempo.close();
                    socket.close();
                    if (this.tiempoUltModif < tiempoUltimaActualizacion) {
                        
                        Socket socketDest = new Socket();
                        InetSocketAddress addr2 = new InetSocketAddress(IPDirectorio, this.puertoDirectorioDestinatarios);
                        socketDest.connect(addr2, 500);
                        ObjectInputStream inDest = new ObjectInputStream(socketDest.getInputStream());
                        
                        Collection<Receptor> destinatariosRegistrados;
                        destinatariosRegistrados = (Collection<Receptor>) inDest.readObject();

                        ControladorEmisor.getInstance().setAgenda(destinatariosRegistrados);
                        
                        
                        this.tiempoUltModif = tiempoUltimaActualizacion;
                        inDest.close();
                        socketDest.close();
                    }
                    
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
