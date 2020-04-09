package directorio.red;

import directorio.modelo.Directorio;
import directorio.modelo.DirectorioMain;

import java.io.ObjectOutputStream;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.security.AccessControlContext;

public class RegistroThread extends Thread {
    private int puertoRegistro;

    public RegistroThread(int puertoRegistro) {
        super();
        this.puertoRegistro = puertoRegistro;
    }

    @Override
    public void run() {
        super.run();
        this.escucharRegistros();
    }


    private void escucharRegistros() {
            while (true) {
                try {

                    ServerSocket s = new ServerSocket(this.puertoRegistro);

                    while (true) {
                        System.out.println("Hilo Registro: Esperando una solicitud...");
                        Socket socket = s.accept();
                        System.out.println("Hilo Registro: Solicitud recibida, enviando ID: "+Directorio.getNextID());

                        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                        out.writeObject(Directorio.getNextID());
                        Directorio.incrementNextID();
                            out.close();


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
