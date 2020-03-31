package emisor.red;

import emisor.controlador.ControladorEmisor;

import emisor.modelo.Emisor;
import emisor.modelo.Mensaje;

import emisor.modelo.SistemaEmisor;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.Iterator;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

public class TCPdeEmisor implements Runnable {

    public TCPdeEmisor() {
        super();
    }


    /**
     * Precondicion: El objeto que llega en XML es siempre un comprobante
     */
    public void run() {
        try {
            ServerSocket s = new ServerSocket(SistemaEmisor.getInstance()
                                                           .getEmisor()
                                                           .getPuerto());

            while (true) {
                Socket soc = s.accept();
                XMLDecoder xmlDecoder = new XMLDecoder(soc.getInputStream());


                Comprobante comprobante = (Comprobante) xmlDecoder.readObject();
                this.onConfirmacion(comprobante);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enviarMensaje(Mensaje mensaje) {


        try {

            Iterator<Receptor> receptores = mensaje.getReceptores();


            while (receptores.hasNext()) {
                Receptor receptorActual= receptores.next();
                    
                Socket socket = new Socket(receptorActual.getIP(), receptorActual.getPuerto());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out.writeObject(mensaje);
                out.close();
                socket.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onConfirmacion(Comprobante comprobante) {
        ControladorEmisor.agregarComprobante(comprobante);
    }

}
