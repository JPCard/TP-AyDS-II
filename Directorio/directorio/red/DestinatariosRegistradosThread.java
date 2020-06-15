package directorio.red;

import directorio.modelo.IDirectorio;

import directorio.modelo.IDirectorio;

import emisor.controlador.ControladorEmisor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.security.AccessControlContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;

import java.util.TreeMap;
import java.util.TreeSet;

import receptor.modelo.IDatosReceptor;

public class DestinatariosRegistradosThread extends Thread {
    private IDirectorio directorio;
    private int getDestinatariosPort;


    public DestinatariosRegistradosThread(IDirectorio directorio) {
        super();
        this.directorio = directorio;
        this.getDestinatariosPort = directorio.getPuertoRecibeGetDestinatarios();
    }

    @Override
    public void run() {
        super.run();
        this.escucharEmisores();
    }


    private void escucharEmisores() {
        while (true) {
            try (ServerSocket s = new ServerSocket(getDestinatariosPort)) {
                while (true) {
                    System.out.println("Hilo Destinatarios: Esperando una solicitud...");
                    try (Socket socket = s.accept()) {
                        System.out.println("Hilo Destinatarios: Solicitud recibida, enviando destinatarios");
//                        System.out.println(directorio.listaDestinatariosRegistrados().toString());
                        if (socket.isConnected()) {
                            try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
                                out.writeObject(directorio.listaDestinatariosRegistrados());
                            }
                        }

                    }
                }

            } 
            
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


