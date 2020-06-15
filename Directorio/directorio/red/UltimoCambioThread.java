package directorio.red;

import directorio.modelo.IDirectorio;

import directorio.modelo.IDirectorio;

import java.io.IOException;
import java.io.ObjectOutputStream;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class UltimoCambioThread extends Thread {
    private IDirectorio directorio;
    private final int ULTIMOCAMBIO_PORT;

    @Override
    public void run() {
        super.run();
        this.escuchar();
    }

    public UltimoCambioThread(IDirectorio directorio) {
        super();
        this.directorio = directorio;
        this.ULTIMOCAMBIO_PORT = directorio.getPuertoRecibeGetUltimoCambio();
    }


    private void escuchar() {
        while (true) {
            try (ServerSocket s = new ServerSocket(ULTIMOCAMBIO_PORT)) {


                while (true) {
                    System.out.println("Hilo Comunicador de Ultimo Cambio: Esperando una solicitud...");
                    try (Socket socket = s.accept()) {
                        System.out.println("Hilo Comunicador de Ultimo Cambio: Solicitud recibida, enviando tiempo de ultimo cambio");
                        if (socket.isConnected()) {
                            System.out.println("=============================1");
                            try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
                                System.out.println("=============================2");
                                directorio.listaDestinatariosRegistrados(); // este va antes para q el tiempo de ultima modificacion este actualizado
                                //
                                System.out.println("=============================3");

                                //nuevo mandatiempos 7000
                                out.writeObject(directorio.getTiempoUltModif()); //siempre en millis
                                System.out.println("=============================4");
                            }
                        } else
                            System.out.println("error de conexion en ultimocambiothread");


                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
