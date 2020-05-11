package servidormensajeria.modelo;

import emisor.modelo.Mensaje;
import emisor.modelo.SistemaEmisor;

import java.io.IOException;
import java.io.ObjectOutputStream;

import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.Iterator;

import receptor.modelo.Receptor;

public class MensajeHandler implements Runnable {
    private Mensaje mensaje;

    public MensajeHandler(Mensaje mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public void run() {
        //SistemaServidor.getInstance()
        //               .getPersistencia()
        //               .guardarMsj(mensaje); //TODO esto hay que implementarlo jajaaaa
        //todo guardar los usuarios de los receptores a los que fue entregado el mensaje
        try {
            Iterator<String> usuarios = mensaje.getReceptores();
            while (usuarios.hasNext()) {
                String usuarioActual = usuarios.next();
                
                Receptor receptorActual = SistemaServidor.getInstance().getReceptor(usuarioActual);
                
                if(receptorActual != null){
                    System.out.println("le voy a mandar a este tipo");
                    System.out.println(receptorActual);
                    System.out.println(receptorActual.getIP());
                    System.out.println(receptorActual.getPuerto());
                
                    
                    Socket socket = new Socket();
                    InetSocketAddress addr = new InetSocketAddress(receptorActual.getIP(), receptorActual.getPuerto());
                    socket.connect(addr, 500);
                    System.out.println("toy por mandar el object!");
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeObject(mensaje);
                    System.out.println("lo escribi!!!");
                    out.close();
                    socket.close();
                }
                //else no hago nada porque el receptor estaba desconectado
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
