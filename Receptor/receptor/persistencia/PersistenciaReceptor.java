package receptor.persistencia;

import java.beans.XMLDecoder;

import java.beans.XMLEncoder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.FileOutputStream;

import receptor.modelo.Receptor;

public class PersistenciaReceptor implements IPersistenciaReceptor{
    private static final String RECEPTOR_FILE_PATH = "ParametrosReceptor.xml";
    public static final String IPDIRECTORIO_FILE_PATH = "IPDirectorio.xml";
    public static final String HEARTBEAT_PORT_FILE_PATH = "PuertoHeartbeat.xml";
    public static final String REGISTRO_PORT_FILE_PATH = "PuertoRegistro.xml";
    
    public PersistenciaReceptor() {
        super();
    }

    @Override
    public Receptor cargarReceptor() throws FileNotFoundException {
        return (Receptor) cargarObjeto(RECEPTOR_FILE_PATH);
    }
    
    @Override
    public String cargarIPDirectorio() throws FileNotFoundException {
        return (String) cargarObjeto(IPDIRECTORIO_FILE_PATH);
    }
    
    @Override
    public int cargarPuertoHeartbeat() throws FileNotFoundException {
        return (Integer) cargarObjeto(HEARTBEAT_PORT_FILE_PATH);
    }
    
    @Override
    public int cargarPuertoRegistro() throws FileNotFoundException {
        return (Integer) cargarObjeto(REGISTRO_PORT_FILE_PATH);
    }
    
    private Object cargarObjeto(String path) throws FileNotFoundException {
        XMLDecoder decoder;
        
        decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(path)));
        Object obj = decoder.readObject();
        decoder.close();
        return obj;
    }
    
}
