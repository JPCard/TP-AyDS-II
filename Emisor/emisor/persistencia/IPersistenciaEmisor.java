package emisor.persistencia;

import emisor.modelo.Agenda;
import emisor.modelo.Emisor;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.parser.ParseException;

public interface IPersistenciaEmisor {
    public Emisor cargarEmisor() throws IOException, ParseException;
    public String cargarIPDirectorio() throws IOException, ParseException;
    public int cargarPuertoDirectorio() throws IOException, ParseException;
    public String cargarIPServidorMensajeria() throws IOException, ParseException;
    public int cargarPuertoServidorMensajeria() throws IOException, ParseException ;
    
}
