package emisor.red;

import emisor.modelo.IMensaje;
import emisor.modelo.IMensaje;
import emisor.modelo.SistemaEmisor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.Collection;

import java.util.HashMap;
import java.util.Iterator;

import receptor.modelo.Comprobante;

public class TCPMensajesPendientes implements Runnable{
    private String ipServidorMensajeria;
    private int puertoServidorMensajeria;
    
    
    public TCPMensajesPendientes(String ipServidorMensajeria, int puertoServidorMensajeria) {
        this.ipServidorMensajeria = ipServidorMensajeria;
        this.puertoServidorMensajeria = puertoServidorMensajeria;
    }


    @Override
    public void run() {
        boolean hayParaEnviar = true;

        while (hayParaEnviar){
            try {
                Socket socket = new Socket();
                InetSocketAddress addr =
                    new InetSocketAddress(this.ipServidorMensajeria, this.puertoServidorMensajeria);
                socket.connect(addr, 500);

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                
                Collection<IMensaje> mensajesPostCifrado = SistemaEmisor.getInstance().getMensajesNoEnviados();
                
                int cantMensajes = mensajesPostCifrado.size();
                //OUT 1
                out.writeObject(cantMensajes); //le digo cuantos son para que me mande esas ID
                
                //IN 1
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                
                Iterator<IMensaje> itMensajesCifrados = mensajesPostCifrado.iterator();
                int nextId;
                int viejaId;
                
                HashMap<Integer,Integer> cambiadorDeIds = new HashMap<Integer,Integer>(); //<ViejaId,NuevaId>
                
                while(itMensajesCifrados.hasNext()){
                    IMensaje mensaje = itMensajesCifrados.next();
                    viejaId = mensaje.getId();
                    
                    System.err.println("el usuario de este mensaje es (receptor): +"+mensaje.getReceptorObjetivo());
                    nextId = (Integer) in.readObject();
                    
                    //actualiza la id real para que pueda recibir comprobantes
                    cambiadorDeIds.put(viejaId,nextId);
                    
                    System.out.println("vieja id = " + viejaId);
                    System.out.println("nueva id = " + nextId);
                    
                    mensaje.setId(nextId);
                }
                
                //OUT 2
                out.writeObject(mensajesPostCifrado);
                out.close();
                
                for(Integer i:cambiadorDeIds.keySet()){
                    SistemaEmisor.getInstance().actualizarIdMensaje(i,cambiadorDeIds.get(i));
                }
                
                SistemaEmisor.getInstance().marcarMensajesPendientesComoEnviados(mensajesPostCifrado);
                
                hayParaEnviar = SistemaEmisor.getInstance().quedanMensajesPendientes();
                
            } catch (IOException e) {
                System.out.println("Hilo enviar mensajes pendientes: Servidor de Mensajeria Fuera de linea. Reintentando...");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException f) {
                }
            } catch (ClassNotFoundException e) {
            }
        }
        
    }
}
