package emisor.red;

import emisor.controlador.ControladorEmisor;

import emisor.modelo.Mensaje;

import emisor.modelo.MensajeConComprobante;
import emisor.modelo.SistemaEmisor;


import java.io.BufferedReader;
import java.io.IOException;
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

import servidormensajeria.modelo.SistemaServidor;

public class TCPdeEmisor implements Runnable {

    private String ipServidorMensajeria;
    private int puertoServidorMensajeria;
    private int puertoServidorMensajeriaSolicitarMensajes;

    public TCPdeEmisor() {
        super();
    }

    public TCPdeEmisor(String ipServidorMensajeria, int puertoServidorMensajeria,
                       int puertoServidorMensajeriaSolicitarMensajes) {
        this.ipServidorMensajeria = ipServidorMensajeria;
        this.puertoServidorMensajeria = puertoServidorMensajeria;
        this.puertoServidorMensajeriaSolicitarMensajes = puertoServidorMensajeriaSolicitarMensajes;
    }


    /**
     * Precondicion: El objeto que llega en XML es siempre un comprobante
     */
    public void run() {
        try {
            SistemaEmisor.getInstance().inicializarMensajesConComprobante();
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
            System.out.println("error de emisor: el puerto de recepcion esta ocupado");
            System.exit(1);
        } catch (Exception e) {
            System.out.println("algo mas general");
            e.printStackTrace();
        }
    }

    public Collection<MensajeConComprobante> solicitarMensajesEnviados() {
        Socket socket = new Socket();
        InetSocketAddress addr =
            new InetSocketAddress(this.ipServidorMensajeria, this.puertoServidorMensajeriaSolicitarMensajes);

        Collection<MensajeConComprobante> mensajesConComprobantePropios = null;
        System.out.println("hastaan tes del while");
        
        boolean leido = false;
        
        while (!leido)
            try {
                socket.connect(addr, 500);
                
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(SistemaEmisor.getInstance()
                                .getEmisor()); //envio al emisor la id con la cual debe rotular su mensaje
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                mensajesConComprobantePropios = (Collection<MensajeConComprobante>) in.readObject();
                out.close();
                in.close();
                socket.close();
                leido=true;
            } catch (IOException e) {
            } catch (ClassNotFoundException e) {
            }
        return mensajesConComprobantePropios;
    }


    public boolean enviarMensaje(Mensaje mensaje) {
        try {
            Socket socket = new Socket();
            InetSocketAddress addr = new InetSocketAddress(this.ipServidorMensajeria, this.puertoServidorMensajeria);
            socket.connect(addr, 500);

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            mensaje.setId((Integer) in.readObject());
//            System.out.println("me llego la id para setear al mensaje: esta es");
//            System.out.println(mensaje.getId());

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(mensaje);
            out.close();
            return true;

        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }

    }

    public void onConfirmacion(Comprobante comprobante) {
        ControladorEmisor.getInstance().agregarComprobante(comprobante);
    }
}
