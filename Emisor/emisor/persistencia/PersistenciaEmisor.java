package emisor.persistencia;

import emisor.modelo.Agenda;
import emisor.modelo.Emisor;

import java.beans.XMLDecoder;

import java.beans.XMLEncoder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class PersistenciaEmisor implements IPersistenciaEmisor {
    public static final String PARAMETROS_FILE_PATH = "ParametrosEmisor.xml";
    public static final String IPDIRECTORIO_FILE_PATH = "IPDirectorio.xml";
    public static final String PUERTO_GET_DEST_PATH = "PuertoGetDestinatarios.xml";
    
    @Override
    public Emisor cargarEmisor() throws FileNotFoundException {
        return (Emisor) cargarObjeto(PARAMETROS_FILE_PATH);
    }

    @Override
    public String cargarIPDirectorio() throws FileNotFoundException {
        return (String) cargarObjeto(IPDIRECTORIO_FILE_PATH);
    }


    @Override
    public int cargarPuertoGetDestinatarios() throws FileNotFoundException {
        int puertoGetDest;
        XMLDecoder decoder;
        
        decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(PUERTO_GET_DEST_PATH)));
        puertoGetDest = (Integer) decoder.readObject();
        decoder.close();
        return puertoGetDest;
    }

    private Object cargarObjeto(String path) throws FileNotFoundException {
        XMLDecoder decoder;
        
        decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(path)));
        Object obj = decoder.readObject();
        decoder.close();
        return obj;
    }

    
}
