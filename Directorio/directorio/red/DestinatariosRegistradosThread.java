package directorio.red;

import directorio.modelo.Directorio;

import emisor.controlador.ControladorEmisor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.security.AccessControlContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;

import java.util.TreeMap;
import java.util.TreeSet;

import receptor.modelo.Receptor;

public class DestinatariosRegistradosThread extends Thread {
    private Directorio directorio;
    private int getDestinatariosPort;


    public DestinatariosRegistradosThread(Directorio directorio) {
        super();
        this.directorio = directorio;
        this.getDestinatariosPort = directorio.getPuertoRecibeGetDestinatarios();
    }

    @Override
    public void run() {
        super.run();
        this.escucharEmisores();
    }


    private void escucharEmisores() {
        this.cargarListaDestinatariosRegistrados();
        while (true) {
            try {

                ServerSocket s = new ServerSocket(getDestinatariosPort);

                while (true) {
                    System.out.println("Hilo Destinatarios: Esperando una solicitud...");
                    Socket socket = s.accept();
                    System.out.println("Hilo Destinatarios: Solicitud recibida, enviando destinatarios");
                    ObjectOutputStream out = null;
                    if (socket.isConnected()) {
                        out = new ObjectOutputStream(socket.getOutputStream());
                        out.writeObject(directorio.listaDestinatariosRegistrados());
                        out.close();
                    }


                    socket.close();
                }

            } catch (BindException e) { //IP y puerto ya estaban utilizados
                //System.exit(1); no lo dejamos cerrar
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
    }

    /**Se utiliza la primera vez que se abre el directorio, para ponerlo al dia sobre quien esta conectado
     * Intenta contactar al otro directorio para obtener los destinatarios que hay hasta ahora, si no lo encuentra comienza de cero
     */
    public void cargarListaDestinatariosRegistrados() {
        Collection<Receptor> destinatariosRegistrados;
        Long tiempoUltModif;
        try {
            Socket socket = new Socket();
            InetSocketAddress addr =
                new InetSocketAddress(directorio.getIpOtroDirectorio(),
                                      this.directorio.getOtroDirectorioPuertoUltimoCambio());
            socket.connect(addr, 500);
            ObjectInputStream inTiempo = new ObjectInputStream(socket.getInputStream());
            tiempoUltModif = (Long) inTiempo.readObject();
            inTiempo.close();
            socket.close();
            
            Socket socketDest = new Socket();
            InetSocketAddress addr2 =
                new InetSocketAddress(directorio.getIpOtroDirectorio(),
                                      directorio.getOtroDirectorioPuertoDestinatarios());
            socketDest.connect(addr2, 500);
            ObjectInputStream inDest = new ObjectInputStream(socketDest.getInputStream());

            destinatariosRegistrados = (Collection<Receptor>) inDest.readObject();


            
            inDest.close();
            socketDest.close();
            
            

        } catch (IOException e) {
            tiempoUltModif = new GregorianCalendar().getTimeInMillis();
            destinatariosRegistrados = new ArrayList<Receptor>();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            tiempoUltModif = new GregorianCalendar().getTimeInMillis();
            destinatariosRegistrados = new ArrayList<Receptor>();
        }
        
        directorio.setTiempoUltModif(tiempoUltModif);
        
        TreeMap<String,Receptor> receptores = new TreeMap<String,Receptor>();
        
        for(Receptor r: destinatariosRegistrados){
            receptores.put(r.getUsuario(),r);
        }
        
        
        directorio.setReceptores(receptores);
        
        
    }

}


