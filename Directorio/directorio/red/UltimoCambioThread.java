package directorio.red;

import directorio.modelo.Directorio;

import java.io.ObjectOutputStream;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class UltimoCambioThread extends Thread {
    private Directorio directorio;
    private final int ULTIMOCAMBIO_PORT;


    @Override
    public void run() {
        super.run();
        this.escuchar();
    }

    public UltimoCambioThread(Directorio directorio) {
        super();
        this.directorio = directorio;
        this.ULTIMOCAMBIO_PORT = directorio.getPuertoRecibeGetUltimoCambio();
    }
    
    
    private void escuchar() {
        while (true) {
            try {

                ServerSocket s = new ServerSocket(ULTIMOCAMBIO_PORT);

                while (true) {
                    System.out.println("Hilo Comunicador de Ultimo Cambio: Esperando una solicitud...");
                    Socket socket = s.accept();
                    System.out.println("Hilo Comunicador de Ultimo Cambio: Solicitud recibida, enviando tiempo de ultimo cambio");
                    ObjectOutputStream out = null;
                    if (socket.isConnected()) {
                        out = new ObjectOutputStream(socket.getOutputStream());
                        
                        directorio.listaDestinatariosRegistrados();// este va antes para q el tiempo de ultima modificacion este actualizado
                        //nuevo mandatiempos 7000
                        out.writeObject(directorio.getTiempoUltModif()); //siempre en millis
                        
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
