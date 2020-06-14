package emisor.red;

import emisor.controlador.ControladorEmisor;

import emisor.modelo.AbstractMensajeFactory;
import emisor.modelo.Mensaje;

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


import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

import servidormensajeria.modelo.SistemaServidor;

public class TCPdeEmisor implements Runnable {

    private String ipServidorMensajeria;
    private int puertoServidorMensajeria;
    private int puertoServidorMensajeriaSolicitarMensajes;
    
    private Thread TCPMensajesPendientes;


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
        while (true) {
            try (ServerSocket s = new ServerSocket(SistemaEmisor.getInstance().getPuerto())){
                SistemaEmisor.getInstance().cargarComprobantesAsincronicos();
                 
                while (true) {
                    try(Socket socket = s.accept()){
                        try(ObjectInputStream in = new ObjectInputStream(socket.getInputStream())){
                    Comprobante comprobante = (Comprobante) in.readObject();
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



    /**
     * Pre: hay una relacion 1:1 entre mensajesPreCifrado y Post, solo llegan hasta aca para recibir una ID
     * @param mensajesPreCifrado
     * @param mensajesPostCifrado
     * @return
     */
    public boolean enviarMensaje(Collection<Mensaje> mensajesPreCifrado,Collection<Mensaje> mensajesPostCifrado) {
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
            
            Iterator<Mensaje> itMensajesCifrados = mensajesPostCifrado.iterator();
            Iterator<Mensaje> itMensajesPreCifrados = mensajesPreCifrado.iterator();
            int nextId;
            Mensaje mensajeActual;
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

    public String getIpServidorMensajeria() {
        return ipServidorMensajeria;
    }

    public int getPuertoServidorMensajeria() {
        return puertoServidorMensajeria;
    }


    public Collection<Comprobante> solicitarComprobantesAsincronicos() {
        Collection<Comprobante> comprobantes = null;

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
                       comprobantes = (Collection<Comprobante>) in.readObject();
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
