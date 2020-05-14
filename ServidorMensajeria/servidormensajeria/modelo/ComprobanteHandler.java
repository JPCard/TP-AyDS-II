package servidormensajeria.modelo;

import emisor.modelo.Emisor;

import java.io.ObjectOutputStream;

import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.Iterator;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

public class ComprobanteHandler implements Runnable {
    //todo completar este
    private Comprobante comprobante;


    public ComprobanteHandler(Comprobante comprobante) {
        this.comprobante = comprobante;
    }

    @Override
    public void run() {
        try {
            Emisor emisor = comprobante.getEmisorOriginal();

            Socket socket = new Socket();
            InetSocketAddress addr = new InetSocketAddress(emisor.getIP(),emisor.getPuerto());
            socket.connect(addr, 500);
            System.out.println("toy por mandar el object!");
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(this.comprobante);
            System.out.println("lo escribi!!!");
            out.close();
            socket.close();

            //else no hago nada porque el receptor estaba desconectado

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
