package emisor.persistencia;

import emisor.modelo.Agenda;
import emisor.modelo.Emisor;

import java.beans.XMLDecoder;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class PersistenciaEmisor implements IPersistenciaEmisor {
    public static final String AGENDA_FILE_PATH = "Agenda.xml";
    public static final String PARAMETROS_FILE_PATH = "ParametrosEmisor.xml";
    
    @Override
    public Emisor cargarEmisor() throws FileNotFoundException {
        Emisor emisor = null;
        XMLDecoder decoder;
        
        decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(PARAMETROS_FILE_PATH)));
        emisor = (Emisor) decoder.readObject();
        return emisor;
    }

//    @Override
//    public Agenda cargarAgenda() {
//        Agenda agenda = null;
//        try {
//            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(AGENDA_FILE_PATH)));
//            agenda = (Agenda) decoder.readObject();
//        } 
//        catch (FileNotFoundException e) {
//            //si no estaba el archivo no carga nada
//        }
//        catch (ClassCastException e) {
//            //si no estaba guardado un objeto tipo ArrayList<Receptor> en el archivo no hace nada
//        }
//        return agenda;
//    }
}
