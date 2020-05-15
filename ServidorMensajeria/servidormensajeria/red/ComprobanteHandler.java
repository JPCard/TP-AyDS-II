package servidormensajeria.red;

import emisor.modelo.Emisor;

import java.io.ObjectOutputStream;

import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.Iterator;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

import servidormensajeria.modelo.SistemaServidor;

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
//            System.out.println("BORRAME"+this.comprobante.getEmisorOriginal().getPuerto()+" asdsada "+this.comprobante.getEmisorOriginal().getIP());
            InetSocketAddress addr = new InetSocketAddress(emisor.getIP(),emisor.getPuerto());
            socket.connect(addr, 500);
//            System.out.println("toy por mandar el object!");
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(this.comprobante);
//            System.out.println("lo escribi!!!");
            out.close();
            socket.close();

            SistemaServidor.getInstance().guardarComp(comprobante);
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
