package directorio.red;

import directorio.modelo.Directorio;

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

import receptor.modelo.Receptor;

public class DestinatariosRegistradosThread extends Thread {
    private Directorio directorio;
    private int getDestinatariosPort;

    private ServerSocket s;
    private Socket socket;
    private ObjectOutputStream out;

    public DestinatariosRegistradosThread(Directorio directorio) {
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
            try {

                s = new ServerSocket(getDestinatariosPort);

                while (true) {
                    System.out.println("Hilo Destinatarios: Esperando una solicitud...");
                    socket = s.accept();
                    System.out.println("Hilo Destinatarios: Solicitud recibida, enviando destinatarios");
                    System.out.println(directorio.listaDestinatariosRegistrados().toString());
                    out = null;
                    if (socket.isConnected()) {
                        out = new ObjectOutputStream(socket.getOutputStream());
                        out.writeObject(directorio.listaDestinatariosRegistrados());
                        out.close();
                    }


                    socket.close();
                }

            } catch (BindException e) { //IP y puerto ya estaban utilizados
                //System.exit(1); no lo dejamos cerrar
            } catch (Exception e) {
                System.err.println("Capturada EOFException");
                try {
                    if (out != null)
                        out.close();
                    if (socket != null)
                        socket.close();
                    if (s != null)
                        s.close();

                } catch (IOException f) {f.printStackTrace();
                    System.err.println("esto si es malo");
                }
            }
        }
    }


    @Override
    protected void finalize() throws Throwable {
        
        super.finalize();
        try {
            if (out != null)
                out.close();
            if (socket != null)
                socket.close();
            if (s != null)
                s.close();

        } catch (IOException f) {f.printStackTrace();
            System.err.println("esto si es malo");
        }
    }
}


