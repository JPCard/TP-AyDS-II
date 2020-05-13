package servidormensajeria.persistencia;

public interface IPersistenciaParametrosServidor {
    public static final String METODO_PERSIST_XML = "XML";
    public static final String METODO_PERSIST_JSON = "JSON";
    public static final String METODO_PERSIST_TXT = "TXT";
    
    public String cargarIPDirectorio() throws Exception;

    public int cargarPuertoDirectorioDestinatarios() throws Exception;

    public int cargarPuertoDirectorioTiempoUltModif() throws Exception;
    
    public String cargarMetodoPersistenciaMsjs() throws Exception;
}
