package emisor.red;

import emisor.controlador.ControladorEmisor;

import emisor.modelo.Mensaje;

import emisor.modelo.SistemaEmisor;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Collection;
import java.util.Iterator;


import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

public class TCPdeEmisor implements Runnable {

    private String ipServidorMensajeria;
    private int puertoServidorMensajeria;

    public TCPdeEmisor() {
        super();
    }

    public TCPdeEmisor(String ipServidorMensajeria, int puertoServidorMensajeria) {
        this.ipServidorMensajeria = ipServidorMensajeria;
        this.puertoServidorMensajeria = puertoServidorMensajeria;
    }


    /**
     * Precondicion: El objeto que llega en XML es siempre un comprobante
     */
    public void run() {
        try {
            ServerSocket s = new ServerSocket(SistemaEmisor.getInstance().getPuerto());
            while (true) {
                Socket socket = s.accept();
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Comprobante comprobante = (Comprobante) in.readObject();
                ControladorEmisor.getInstance().agregarComprobante(comprobante);
                in.close();
                socket.close();
            }

        } catch (BindException e) { //IP y puerto ya estaban utilizados
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean enviarMensaje(Mensaje mensaje) {
        try {
            Socket socket = new Socket();
            InetSocketAddress addr = new InetSocketAddress(this.ipServidorMensajeria, this.puertoServidorMensajeria);
            socket.connect(addr, 500);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(mensaje);
            out.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public void onConfirmacion(Comprobante comprobante) {
        ControladorEmisor.getInstance().agregarComprobante(comprobante);
    }
}
