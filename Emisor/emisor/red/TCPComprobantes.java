package emisor.red;

import emisor.controlador.ControladorEmisor;

import emisor.modelo.AbstractMensajeFactory;
import emisor.modelo.IMensaje;

import emisor.modelo.ISistemaEmisor;
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
import receptor.modelo.IDatosReceptor;

import servidormensajeria.modelo.SistemaServidor;

public class TCPComprobantes extends RedComprobantes {

    private String ipServidorMensajeria;
    private int puertoServidorMensajeria;
    private int puertoServidorMensajeriaSolicitarMensajes;
    private ISistemaEmisor sistemaEmisor;

    public String getIpServidorMensajeria() {
        return ipServidorMensajeria;
    }

    public int getPuertoServidorMensajeria() {
        return puertoServidorMensajeria;
    }


    public TCPComprobantes(String ipServidorMensajeria, int puertoServidorMensajeria,
                           int puertoServidorMensajeriaSolicitarMensajes, ISistemaEmisor sistemaEmisor) {
        this.ipServidorMensajeria = ipServidorMensajeria;
        this.puertoServidorMensajeria = puertoServidorMensajeria;
        this.puertoServidorMensajeriaSolicitarMensajes = puertoServidorMensajeriaSolicitarMensajes;
        this.sistemaEmisor = sistemaEmisor;
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
                       out.writeObject(sistemaEmisor
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

    @Override
    protected void escucharComprobantes() {
        while (true) {
            try (ServerSocket s = new ServerSocket(sistemaEmisor.getPuerto())){
                sistemaEmisor.cargarComprobantesAsincronicos();
                 
                while (true) {
                    try(Socket socket = s.accept()){
                        try(ObjectInputStream in = new ObjectInputStream(socket.getInputStream())){
                    IComprobante comprobante = (IComprobante) in.readObject();
                    //System.out.println("EL COMPROBANTE ES");
                    //System.out.println(comprobante);
                    ControladorEmisor.getInstance().agregarComprobante(comprobante);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }

            } catch(BindException f){
                System.out.println("Puerto utilizado, cerrando");
                //f.printStackTrace();
                System.exit(1);// puerto ya en uso, se cierra.
            }
            
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
