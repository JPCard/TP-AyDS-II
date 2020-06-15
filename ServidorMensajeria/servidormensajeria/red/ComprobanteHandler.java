package servidormensajeria.red;

import emisor.modelo.IDatosEmisor;

import emisor.modelo.IDatosEmisor;

import java.io.ObjectOutputStream;

import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.Iterator;

import receptor.modelo.IComprobante;
import receptor.modelo.Receptor;

import servidormensajeria.modelo.SistemaServidor;

public class ComprobanteHandler implements Runnable {
    private IComprobante comprobante;


    public ComprobanteHandler(IComprobante comprobante) {
        this.comprobante = comprobante;
    }

    @Override
    public void run() {
        try {
            SistemaServidor.getInstance().guardarComp(comprobante);
            IDatosEmisor emisor = comprobante.getEmisorOriginal();

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

            
            

        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("IComprobante no se pudo enviar al emisor, guardando para ser enviado posteriormente");
            try {
                SistemaServidor.getInstance()
                               .getPersistencia()
                               .guardarComprobanteNoEnviado(comprobante);
            } catch (Exception f) {
                System.out.println("algo terrible ha ocurridoen comprobantehandler");
            }
        }
    }
}
