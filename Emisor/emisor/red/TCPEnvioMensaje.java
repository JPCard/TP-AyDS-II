package emisor.red;

import emisor.modelo.IMensaje;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.Collection;
import java.util.Iterator;

public class TCPEnvioMensaje implements IEnvioMensaje {
    private String ipServidorMensajeria;
    private int puertoServidorMensajeria;


    public TCPEnvioMensaje(String ipServidorMensajeria, int puertoServidorMensajeria) {
        this.ipServidorMensajeria = ipServidorMensajeria;
        this.puertoServidorMensajeria = puertoServidorMensajeria;
    }

    /**
     * Pre: hay una relacion 1:1 entre mensajesPreCifrado y Post, solo llegan hasta aca para recibir una ID
     * @param mensajesPreCifrado
     * @param mensajesPostCifrado
     * @return
     */
    public boolean enviarMensaje(Collection<IMensaje> mensajesPreCifrado,Collection<IMensaje> mensajesPostCifrado) {
        try {
            Socket socket = new Socket();
            InetSocketAddress addr = new InetSocketAddress(this.ipServidorMensajeria, this.puertoServidorMensajeria);
            socket.connect(addr, 500);

            
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            int cantMensajes = mensajesPostCifrado.size();
            //OUT 1
            out.writeObject(cantMensajes); //le digo cuantos son para que me mande esas ID
            
            //IN 1
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            
            Iterator<IMensaje> itMensajesCifrados = mensajesPostCifrado.iterator();
            Iterator<IMensaje> itMensajesPreCifrados = mensajesPreCifrado.iterator();
            int nextId;
            IMensaje mensajeActual;
            while(itMensajesCifrados.hasNext()){
                nextId = (Integer) in.readObject();
                mensajeActual = itMensajesPreCifrados.next();
                    
                itMensajesCifrados.next().setId(nextId);
                
                mensajeActual.setId(nextId);
            }
            
            
            //OUT 2
            out.writeObject(mensajesPostCifrado);
            out.close();
            return true;

        } catch (Exception e) {
           
                
                
            
           // e.printStackTrace();
            return false;
        }

    }
}
