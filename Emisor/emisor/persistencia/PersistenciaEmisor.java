package emisor.persistencia;

import emisor.modelo.Emisor;

import java.beans.XMLDecoder;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.util.ArrayList;

import receptor.modelo.Receptor;

public class PersistenciaEmisor implements IPersistenciaEmisor {
    public static final String AGENDA_FILE_PATH = "Agenda.xml";

    @Override
    public Emisor cargarEmisor() {
        // TODO Implement this method
        return null;
    }

    @Override
    public Agenda cargarAgenda() {
        Agenda agenda = null;
        try {
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(AGENDA_FILE_PATH)));
            agenda = (Agenda) decoder.readObject();
        } 
        catch (FileNotFoundException e) {
            //si no estaba el archivo no carga nada
        }
        catch (ClassCastException e) {
            //si no estaba guardado un objeto tipo ArrayList<Receptor> en el archivo no hace nada
        }
        return agenda;
    }
}
