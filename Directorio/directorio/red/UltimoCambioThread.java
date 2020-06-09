package directorio.red;

import directorio.modelo.Directorio;

import java.io.IOException;
import java.io.ObjectOutputStream;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class UltimoCambioThread extends Thread {
    private Directorio directorio;
    private final int ULTIMOCAMBIO_PORT;
    private ServerSocket s;
    private Socket socket;
    private ObjectOutputStream out;

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

                s = new ServerSocket(ULTIMOCAMBIO_PORT);

                while (true) {
                    System.out.println("Hilo Comunicador de Ultimo Cambio: Esperando una solicitud...");
                    socket = s.accept();
                    System.out.println("Hilo Comunicador de Ultimo Cambio: Solicitud recibida, enviando tiempo de ultimo cambio");
                    out = null;
                    if (socket.isConnected()) {
                        System.out.println("=============================1");
                        out = new ObjectOutputStream(socket.getOutputStream());
                        System.out.println("=============================2");
                        System.out.println(directorio.listaDestinatariosRegistrados()); // este va antes para q el tiempo de ultima modificacion este actualizado
                        //el sop es optativo
                        System.out.println("=============================3");

                        //nuevo mandatiempos 7000
                        out.writeObject(directorio.getTiempoUltModif()); //siempre en millis
                        System.out.println("=============================4");
                        out.close();
                    }
                    else
                        System.out.println("error de conexion en ultimocambiothread");


                    socket.close();
                }

            } catch (BindException e) { //IP y puerto ya estaban utilizados
                //System.exit(1); no lo dejamos cerrar
                e.printStackTrace();
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
