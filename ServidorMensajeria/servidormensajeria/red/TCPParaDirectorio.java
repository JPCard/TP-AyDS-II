package servidormensajeria.red;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.lang.reflect.Array;

import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Iterator;

import receptor.modelo.Receptor;

import servidormensajeria.modelo.SistemaServidor;

public class TCPParaDirectorio {

    private String ipDirectorio;
    private int puertoDirectorio;

    public TCPParaDirectorio(String ipDirectorio, int puertoDirectorio) {
        this.ipDirectorio = ipDirectorio;
        this.puertoDirectorio = puertoDirectorio;
    }

    public Receptor getReceptor(String usuarioActual) {
        try {
            Socket socket = new Socket();
            InetSocketAddress addr = new InetSocketAddress(this.ipDirectorio, this.puertoDirectorio);

            socket.connect(addr, 500);
            System.out.println("LLEGUE 1");
            ObjectInputStream in;
            System.out.println("LLEGUE 2");

            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("LLEGUE 3");


            Long tiempoUltimaActualizacion = (Long) in.readObject();
            System.out.println("LLEGUE 4");
            if (SistemaServidor.getInstance().getTiempoUltimaActualizacionReceptores() < tiempoUltimaActualizacion) {

                System.out.println("LLEGUE 5");
                SistemaServidor.getInstance().setReceptores((ArrayList<Receptor>) in.readObject());

                System.out.println("LLEGUE 6");
                SistemaServidor.getInstance().setTiempoUltimaActualizacionReceptores(tiempoUltimaActualizacion);

                System.out.println("LLEGUE 7");
            }
            in.close();

            System.out.println("LLEGUE 8");
            socket.close();

            System.out.println("LLEGUE 9");
            Receptor[] receptoresArray = (Receptor[]) SistemaServidor.getInstance()
                                                                     .getReceptores()
                                                                     .toArray();

            int indice =
                Arrays.binarySearch(receptoresArray, new Receptor("123213", 12312, "AAAAAAAAAA", usuarioActual));
            System.out.println("INDICE INDICE INDICE "+ indice);
            if (indice == -1)
                return null;
            else
                return receptoresArray[indice]; //ya viene ordenada la lista de receptores

        } catch (IOException e) {
            System.out.println("QUILOMBO");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("QUILOMBO DOS");
        }

        return null;
    }
}
