package emisor.red;

import emisor.controlador.ControladorEmisor;

import emisor.modelo.AbstractMensajeFactory;
import emisor.modelo.IMensaje;

import emisor.modelo.MensajeConComprobante;
import emisor.modelo.MensajeFactory;
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


import receptor.modelo.IComprobante;
import receptor.modelo.Receptor;

import servidormensajeria.modelo.SistemaServidor;

public class TCPComprobantes implements IRedComprobantes {

    private String ipServidorMensajeria;
    private int puertoServidorMensajeria;
    private int puertoServidorMensajeriaSolicitarMensajes;


    public String getIpServidorMensajeria() {
        return ipServidorMensajeria;
    }

    public int getPuertoServidorMensajeria() {
        return puertoServidorMensajeria;
    }

    public TCPComprobantes() {
        super();
    }

    public TCPComprobantes(String ipServidorMensajeria, int puertoServidorMensajeria,
                       int puertoServidorMensajeriaSolicitarMensajes) {
        this.ipServidorMensajeria = ipServidorMensajeria;
        this.puertoServidorMensajeria = puertoServidorMensajeria;
        this.puertoServidorMensajeriaSolicitarMensajes = puertoServidorMensajeriaSolicitarMensajes;
    }


    /**
     * Precondicion: El objeto que llega en XML es siempre un comprobante
     */
    public void run() {
        while (true) {
            try (ServerSocket s = new ServerSocket(SistemaEmisor.getInstance().getPuerto())){
                SistemaEmisor.getInstance().cargarComprobantesAsincronicos();
                 
                while (true) {
                    try(Socket socket = s.accept()){
                        try(ObjectInputStream in = new ObjectInputStream(socket.getInputStream())){
                    IComprobante comprobante = (IComprobante) in.readObject();
                    System.out.println("EL COMPROBANTE ES");
                    System.out.println(comprobante);
                    ControladorEmisor.getInstance().agregarComprobante(comprobante);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    



    public Collection<IComprobante> solicitarComprobantesAsincronicos() {
        Collection<IComprobante> comprobantes = null;

               boolean leido = false;

               while (!leido){
                   try {
                       Socket socket = new Socket();
                       InetSocketAddress addr =
                           new InetSocketAddress(this.ipServidorMensajeria, this.puertoServidorMensajeriaSolicitarMensajes);
                       socket.connect(addr, 500);

                       ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                       out.writeObject(SistemaEmisor.getInstance()
                                       .getEmisor()); //envio al emisor la id con la cual debe rotular su mensaje
                       ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                       comprobantes = (Collection<IComprobante>) in.readObject();
                       System.out.println("Hilo recuperador de mensajes con comprobante: Comprobantes recuperados exitosamente");
                       out.close();
                       in.close();
                       socket.close();
                       leido = true;
                   } catch (IOException e) {
                       System.out.println("Hilo recuperador de mensajes con comprobante: Servidor de mensajeria no responde: reintentando");
                       try {
                           Thread.sleep(500);
                       } catch (InterruptedException f) {
                       }
                   } catch (ClassNotFoundException e) {
                   }
               }

               return comprobantes;
    }
}
