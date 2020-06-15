package directorio.red;

import directorio.modelo.IDirectorio;

import directorio.modelo.IDirectorio;

import java.io.IOException;
import java.io.ObjectInputStream;

import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.TreeMap;

import receptor.modelo.IDatosReceptor;

public class SincronizacionInicialDirectorios {
    private IDirectorio directorio;

    public SincronizacionInicialDirectorios(IDirectorio directorio) {
        super();
        this.directorio = directorio;
    }


    /**Se utiliza la primera vez que se abre el directorio, para ponerlo al dia sobre quien esta conectado
     * Intenta contactar al otro directorio para obtener los destinatarios que hay hasta ahora, si no lo encuentra comienza de cero
     */
    public void cargarListaDestinatariosRegistrados() {
        Collection<IDatosReceptor> destinatariosRegistrados;
        Long tiempoUltModif;
        try {
            Socket socket = new Socket();
            InetSocketAddress addr =
                new InetSocketAddress(directorio.getIpOtroDirectorio(),
                                      directorio.getOtroDirectorioPuertoUltimoCambio());
            socket.connect(addr, 500);
            
            if (socket.isConnected()){
                ObjectInputStream inTiempo = new ObjectInputStream(socket.getInputStream());
                tiempoUltModif = (Long) inTiempo.readObject();
                inTiempo.close();
                socket.close();
            }
                
            else tiempoUltModif = new GregorianCalendar().getTimeInMillis();
            
            

            Socket socketDest = new Socket();
            InetSocketAddress addr2 =
                new InetSocketAddress(directorio.getIpOtroDirectorio(),
                                      directorio.getOtroDirectorioPuertoDestinatarios());
            socketDest.connect(addr2, 500);
            ObjectInputStream inDest = new ObjectInputStream(socketDest.getInputStream());

            destinatariosRegistrados = (Collection<IDatosReceptor>) inDest.readObject();


            inDest.close();
            socketDest.close();


        } catch (IOException e) {
            e.printStackTrace();
            tiempoUltModif = new GregorianCalendar().getTimeInMillis();
            destinatariosRegistrados = new ArrayList<IDatosReceptor>();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            tiempoUltModif = new GregorianCalendar().getTimeInMillis();
            destinatariosRegistrados = new ArrayList<IDatosReceptor>();
        }

        directorio.setTiempoUltModif(tiempoUltModif);
        //        directorio.setTiempoUltModif(new GregorianCalendar().getTimeInMillis());


        TreeMap<String, IDatosReceptor> receptores = new TreeMap<String, IDatosReceptor>();

        for (IDatosReceptor r : destinatariosRegistrados) {
            receptores.put(r.getUsuario(), r);
            directorio.heartbeatRecibido(r);
        }


        directorio.setReceptores(receptores);


    }


}
