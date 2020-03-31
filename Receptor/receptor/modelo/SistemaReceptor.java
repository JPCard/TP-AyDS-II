package receptor.modelo;

import java.beans.XMLDecoder;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import receptor.red.TCPdeReceptor;

public class SistemaReceptor {
    private static SistemaReceptor instance = null;
    private Receptor receptor;
    private TCPdeReceptor tcpdeReceptor = new TCPdeReceptor();

    public TCPdeReceptor getTcpdeReceptor() {
        return tcpdeReceptor;
    }

    private SistemaReceptor() throws FileNotFoundException {
        super();
        XMLDecoder decoder;

        decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(RECEPTOR_FILE_PATH)));
        receptor = (Receptor) decoder.readObject();

    }

    public Receptor getReceptor() {
        return receptor;
    }
    private static final String RECEPTOR_FILE_PATH = "ParametrosReceptor.xml";

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
