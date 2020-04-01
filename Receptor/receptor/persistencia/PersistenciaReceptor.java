package receptor.persistencia;

import java.beans.XMLDecoder;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import java.io.FileNotFoundException;

import receptor.modelo.Receptor;

public class PersistenciaReceptor implements IPersistenciaReceptor{
    private static final String RECEPTOR_FILE_PATH = "ParametrosReceptor.xml";
    
    public PersistenciaReceptor() {
        super();
    }

    @Override
    public Receptor cargarReceptor() throws FileNotFoundException {
        Receptor receptor = null;
        XMLDecoder decoder;
        
        decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(RECEPTOR_FILE_PATH)));
        receptor = (Receptor) decoder.readObject();
        return receptor;
    }
}
