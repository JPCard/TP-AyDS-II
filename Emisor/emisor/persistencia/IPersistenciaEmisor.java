package emisor.persistencia;

import emisor.modelo.Agenda;
import emisor.modelo.Emisor;

import java.io.FileNotFoundException;

public interface IPersistenciaEmisor {
    public Emisor cargarEmisor() throws FileNotFoundException ;
    public Agenda cargarAgenda();
}
