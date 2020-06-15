package servidormensajeria.red;

import emisor.modelo.IDatosEmisor;

import emisor.modelo.IDatosEmisor;

import java.io.ObjectOutputStream;

import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.Iterator;

import receptor.modelo.IComprobante;
import receptor.modelo.IDatosReceptor;

import servidormensajeria.modelo.ISistemaServidor;
import servidormensajeria.modelo.SistemaServidor;

public class ComprobanteHandler implements Runnable {
    private ISistemaServidor sistemaServidor;
    private IComprobante comprobante;


    public ComprobanteHandler(ISistemaServidor sistemaServidor, IComprobante comprobante) {
        this.sistemaServidor = sistemaServidor;
        this.comprobante = comprobante;
    }


    @Override
    public void run() {
        try {
            sistemaServidor.guardarComp(comprobante);
            IDatosEmisor emisor = comprobante.getEmisorOriginal();

            Socket socket = new Socket();
            //            System.out.println("BORRAME"+this.comprobante.getEmisorOriginal().getPuerto()+" asdsada "+this.comprobante.getEmisorOriginal().getIP());
            InetSocketAddress addr = new InetSocketAddress(emisor.getIP(), emisor.getPuerto());
            socket.connect(addr, 500);
            //            System.out.println("toy por mandar el object!");
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(this.comprobante);
            //            System.out.println("lo escribi!!!");
            out.close();
            socket.close();


        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("IComprobante no se pudo enviar al emisor, guardando para ser enviado posteriormente");
            try {
                sistemaServidor.getPersistencia().guardarComprobanteNoEnviado(comprobante);
            } catch (Exception f) {
                System.out.println("algo terrible ha ocurridoen comprobantehandler");
            }
        }
    }
}
