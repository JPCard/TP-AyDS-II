package directorio.red;

import directorio.modelo.Directorio;

import java.io.ObjectOutputStream;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import java.security.AccessControlContext;

import java.util.GregorianCalendar;
import java.util.HashMap;

import receptor.modelo.Receptor;

public class DestinatariosRegistradosThread extends Thread {
    private Directorio directorio;
    private int getDestinatariosPort;


    public DestinatariosRegistradosThread(Directorio directorio, int getDestinatariosPort) {
        super();
        this.directorio = directorio;
        this.getDestinatariosPort = getDestinatariosPort;
    }

    @Override
    public void run() {
        super.run();
        this.escucharEmisores();
    }


    private void escucharEmisores() {
        while (true) {
            try {

                ServerSocket s = new ServerSocket(getDestinatariosPort);

                while (true) {
                    System.out.println("Hilo Destinatarios: Esperando una solicitud...");
                    Socket socket = s.accept();
                    System.out.println("Hilo Destinatarios: Solicitud recibida, enviando destinatarios");
                    ObjectOutputStream out = null;
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
                e.printStackTrace();
            }
        }
    }
}
