package directorio.persistencia;

public interface IPersistenciaDirectorio {
    public String cargarIPServidorMensajeria() throws Exception;
    public int cargarPuertoServidorMensajeria() throws Exception;

    public int cargarPuertoRecibeHeartbeats() throws Exception;
    public int cargarPuertoRecibeGetDestinatarios() throws Exception;
    public int cargarPuertoRecibeGetUltimoCambio() throws Exception;

    public int cargarpuertoPushReceptores() throws Exception;
    
    
    public String cargarIPOtroDirectorio()throws Exception;
    public int cargarPuertoHeartbeatsOtroDirectorio()throws Exception;
    public int cargarPuertoGetDestinatariosOtroDirectorio() throws Exception;

    public int cargarPuertoUltimoCambioOtroDirectorio()throws Exception;
}
