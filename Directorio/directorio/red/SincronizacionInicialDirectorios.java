package directorio.red;

import directorio.modelo.Directorio;

import java.io.IOException;
import java.io.ObjectInputStream;

import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.TreeMap;

import receptor.modelo.Receptor;

public class SincronizacionInicialDirectorios{
    private Directorio directorio;
    public SincronizacionInicialDirectorios(Directorio directorio) {
        super();
        this.directorio = directorio;
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
                                      directorio.getOtroDirectorioPuertoUltimoCambio());
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
            e.printStackTrace();
            tiempoUltModif = new GregorianCalendar().getTimeInMillis();
            destinatariosRegistrados = new ArrayList<Receptor>();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            tiempoUltModif = new GregorianCalendar().getTimeInMillis();
            destinatariosRegistrados = new ArrayList<Receptor>();
        }
        
        directorio.setTiempoUltModif(tiempoUltModif);
//        directorio.setTiempoUltModif(new GregorianCalendar().getTimeInMillis());

        
        TreeMap<String,Receptor> receptores = new TreeMap<String,Receptor>();
        
        for(Receptor r: destinatariosRegistrados){
            receptores.put(r.getUsuario(),r);
            directorio.heartbeatRecibido(r);
        }
        
        
        directorio.setReceptores(receptores);
        
        
        
        
    }
    
    
}
