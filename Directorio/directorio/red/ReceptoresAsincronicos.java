package directorio.red;

import directorio.modelo.Directorio;

import java.io.IOException;

import java.io.ObjectOutputStream;

import java.net.InetSocketAddress;
import java.net.Socket;

import receptor.modelo.Receptor;

import servidormensajeria.modelo.SistemaServidor;

public class ReceptoresAsincronicos {
    public ReceptoresAsincronicos() {
        super();
    }

    public static void avisarReceptorSeConecto(Receptor receptor) {
        
        try {
            Socket socket = new Socket();
            InetSocketAddress addr = new InetSocketAddress(Directorio.getInstance().getIpServidorMensajeria(),Directorio.getInstance().getPuertoPushReceptores());
            socket.connect(addr, 500);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(receptor);

            out.close();
            socket.close();
        } catch (IOException e) {
        }


    }
}
