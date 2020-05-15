package servidormensajeria.red;

import emisor.modelo.Mensaje;
import emisor.modelo.SistemaEmisor;

import java.io.IOException;
import java.io.ObjectOutputStream;

import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.Iterator;

import receptor.modelo.Receptor;

import servidormensajeria.modelo.SistemaServidor;

public class MensajeHandler implements Runnable {
    private Mensaje mensaje;

    public MensajeHandler(Mensaje mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public void run() {

        Iterator<String> usuarios = mensaje.getReceptores();
        while (usuarios.hasNext()) {
            String usuarioActual = usuarios.next();

            Receptor receptorActual = SistemaServidor.getInstance().getReceptor(usuarioActual);
            
            boolean enviado;
            if (receptorActual != null) {
//                System.out.println("le voy a mandar a este tipo");
//                System.out.println(receptorActual);
//                System.out.println(receptorActual.getIP());
//                System.out.println(receptorActual.getPuerto());

                
                try {
                    Socket socket = new Socket();
                    InetSocketAddress addr = new InetSocketAddress(receptorActual.getIP(), receptorActual.getPuerto());
                    socket.connect(addr, 500);
//                    System.out.println("toy por mandar el object!");
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeObject(mensaje);
//                    System.out.println("lo escribi!!!");
                    out.close();
                    socket.close();

                    enviado = true;
                } catch (Exception e) {
                    //e.printStackTrace(); no se pudo conectar con el receptor
                    enviado = false;
                }
                try {
                    System.out.println(mensaje);
                    System.out.println(usuarioActual);
                    SistemaServidor.getInstance().guardarMsj(mensaje, usuarioActual, enviado); 
                } catch (Exception f) {
                    f.printStackTrace();
                }

            }
            else{ //receptor desconectado o el directorio no lo conoce
                enviado = false;
                try {
                    SistemaServidor.getInstance().guardarMsj(mensaje, usuarioActual, enviado);
                } catch (Exception e) {
                }
            }


        }
    }
}
