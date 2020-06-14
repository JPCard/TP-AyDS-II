package servidormensajeria.persistencia;

public interface IPersistenciaParametrosServidor {
    
    
    public String cargarIPDirectorio() throws Exception;

    public int cargarPuertoDirectorioDestinatarios() throws Exception;

    public int cargarPuertoDirectorioTiempoUltModif() throws Exception;
    
    public String cargarMetodoPersistenciaMsjs() throws Exception;
    
    public int cargarPuertoRecepcionMensajes()  throws Exception;
    
    public int cargarPuertoComprobantes()  throws Exception;
    
    public int cargarPuertoDevolverMensajesEmisores()  throws Exception;
    
    public int cargarpuertoDirectorioPrincipalPushReceptores() throws Exception;

    public int cargarPuertoDirectorioSecundarioPushReceptores() throws Exception;


    public String cargarIPDirectorioSecundario()throws Exception ;

    public int cargarPuertoDirectorioSecundarioTiempo()throws Exception ;

    public int cargarPuertoDirectorioSecundarioDestinatarios()throws Exception ;
}
