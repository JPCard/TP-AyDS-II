package emisor.modelo;


public abstract class MensajeFactory {
    public enum TipoMensaje {
        MSJ_NORMAL,
        MSJ_ALERTA,
        MSJ_CON_COMPROBANTE;
    }
    
    public MensajeFactory() {
        super();
        //&aaasdasd
    }
    
    public Mensaje crearMensaje(String asunto, String cuerpo, TipoMensaje tipoMensaje){
        return null;
    }
}
