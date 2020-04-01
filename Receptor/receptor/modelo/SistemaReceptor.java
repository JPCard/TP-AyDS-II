package receptor.modelo;

import java.beans.XMLDecoder;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import receptor.persistencia.PersistenciaReceptor;

import receptor.red.TCPdeReceptor;

public class SistemaReceptor {
    private static SistemaReceptor instance = null;
    private Receptor receptor;
    private TCPdeReceptor tcpdeReceptor = new TCPdeReceptor();
    private PersistenciaReceptor persistencia = new PersistenciaReceptor();

    public TCPdeReceptor getTcpdeReceptor() {
        return tcpdeReceptor;
    }

    private SistemaReceptor() throws FileNotFoundException {
        super();
        this.receptor = persistencia.cargarReceptor();

    }

    public Receptor getReceptor() {
        return receptor;
    }
    

    public static void inicializar() throws FileNotFoundException {
        if (instance == null)
            instance = new SistemaReceptor();
        
        Thread t = new Thread(instance.tcpdeReceptor);
        t.start();
    }

    public static SistemaReceptor getInstance() {
        return instance;
    }
    
    public int getPuerto(){
        return this.receptor.getPuerto();
    }
}
