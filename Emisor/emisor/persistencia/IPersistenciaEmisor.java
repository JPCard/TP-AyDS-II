package emisor.persistencia;

import emisor.modelo.IDatosEmisor;

import java.io.IOException;

import org.json.simple.parser.ParseException;


public interface IPersistenciaEmisor {
    
    //dejamos que tiren excepciones para que luego puedan tirar cualquier excepcion que la clase concreta necesite.
    public IDatosEmisor cargarEmisor() throws Exception;
    
    public String cargarIPServidorMensajeria() throws Exception;
    public int cargarPuertoServidorMensajeria() throws Exception;

    public String cargarIPDirectorio() throws Exception;
    public int cargarPuertoDirectorioTiempo()throws Exception;
    public int cargarPuertoDirectorioDest()throws Exception;
    
    public String cargarIPDirectorioSecundario() throws Exception;
    public int cargarPuertoDirectorioSecundarioTiempo() throws Exception;
    public int cargarPuertoDirectorioSecundarioDest() throws Exception;

    public int cargarPuertoServidorSolicitarMensajesEmisor()throws Exception;
}
