package servidormensajeria.red;

import emisor.controlador.ControladorEmisor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.lang.reflect.Array;

import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import java.util.TreeSet;

import receptor.modelo.Receptor;

import servidormensajeria.modelo.SistemaServidor;

public class TCPParaDirectorio {

    private String ipDirectorio;
    private int puertoDirectorioTiempo;
    private int puertoDirectorioDestinatarios;

    public TCPParaDirectorio(String ipDirectorio, int puertoDirectorioDestinatarios,int puertoDirectorioTiempo) {
        this.ipDirectorio = ipDirectorio;
        this.puertoDirectorioTiempo = puertoDirectorioTiempo;
        this.puertoDirectorioDestinatarios = puertoDirectorioDestinatarios;
    }

    public Receptor getReceptor(String usuarioActual) {
        try {
            Socket socketTiempo = new Socket();
            InetSocketAddress addrTiempo = new InetSocketAddress(this.ipDirectorio, this.puertoDirectorioTiempo);

            socketTiempo.connect(addrTiempo, 500);
            System.out.println("LLEGUE 1");
            ObjectInputStream in;
            System.out.println("LLEGUE 2");

            in = new ObjectInputStream(socketTiempo.getInputStream());
            System.out.println("LLEGUE 3");


            Long tiempoUltimaActualizacion = (Long) in.readObject();
            in.close();
            socketTiempo.close();
            
            System.out.println("LLEGUE 4");
            if (SistemaServidor.getInstance().getTiempoUltimaActualizacionReceptores() < tiempoUltimaActualizacion) {


                Socket socketDest = new Socket();
                InetSocketAddress addr2 = new InetSocketAddress(ipDirectorio, this.puertoDirectorioDestinatarios);
                socketDest.connect(addr2, 500);
                ObjectInputStream inDest = new ObjectInputStream(socketDest.getInputStream());
                
                
                TreeSet<Receptor> t = ( TreeSet<Receptor>)inDest.readObject();
                SistemaServidor.getInstance().setReceptores(new ArrayList<Receptor>(t));
                
                
                inDest.close();
                socketDest.close();



                System.out.println("LLEGUE 5");
                

                System.out.println("LLEGUE 6");
                SistemaServidor.getInstance().setTiempoUltimaActualizacionReceptores(tiempoUltimaActualizacion);

                System.out.println("LLEGUE 7");
            }
            

            System.out.println("LLEGUE 9");//todo hacer algo mas eficiente aca
            ArrayList<Receptor> receptoresArray =  SistemaServidor.getInstance()
                                                                     .getReceptores();

            
            int indice =receptoresArray.indexOf(new Receptor("123213", 12312, "AAAAAAAAAA", usuarioActual));
                //Arrays.binarySearch(receptoresArray, new Receptor("123213", 12312, "AAAAAAAAAA", usuarioActual));
            System.out.println("INDICE INDICE INDICE "+ indice);
            if (indice == -1)
                return null;
            else
                return receptoresArray.get(indice); //ya viene ordenada la lista de receptores

        } catch (IOException e) {
            System.out.println("QUILOMBO");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("QUILOMBO DOS");
        }

        return null;
    }
}
