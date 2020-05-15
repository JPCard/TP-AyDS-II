package servidormensajeria.persistencia;

public interface IPersistenciaParametrosServidor {
    
    
    public String cargarIPDirectorio() throws Exception;

    public int cargarPuertoDirectorioDestinatarios() throws Exception;

    public int cargarPuertoDirectorioTiempoUltModif() throws Exception;
    
    public String cargarMetodoPersistenciaMsjs() throws Exception;
    
    
}
