package directorio.red;

import directorio.modelo.IDirectorio;

import directorio.modelo.IDirectorio;

import java.io.IOException;

import java.io.ObjectOutputStream;

import java.net.InetSocketAddress;
import java.net.Socket;

import receptor.modelo.IDatosReceptor;

import servidormensajeria.modelo.SistemaServidor;

public class ReceptoresAsincronicos {
    private IDirectorio directorio;

    public ReceptoresAsincronicos(IDirectorio directorio) {
        this.directorio = directorio;
    }


    public void avisarReceptorSeConecto(IDatosReceptor receptor) {
        
        try {
            Socket socket = new Socket();
            InetSocketAddress addr = new InetSocketAddress(directorio.getIpServidorMensajeria(),directorio.getPuertoPushReceptores());
            socket.connect(addr,1500);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(receptor);

            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
