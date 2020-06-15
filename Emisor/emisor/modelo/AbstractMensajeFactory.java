package emisor.modelo;


import java.util.ArrayList;

public abstract class AbstractMensajeFactory {
    public enum TipoMensaje {
        MSJ_NORMAL,
        MSJ_CON_ALERTA,
        MSJ_CON_COMPROBANTE;
    }
    
    /**
     * @param emisor - quien envia el mensaje
     * @param asunto - de que trata el mensaje
     * @param cuerpo - contenido del mensaje
     * @param tipoMensaje - 
     * @return - devuelve el mensaje con el contenido y tipo especificado
     * 
     * <b>Pre:</b> emisor != null, asunto != null y asunto != "", cuerpo != null, cuerpo != "".
     * <b>Post:</b> se crea el mensaje con el contenido y tipo especificados y es retornado.
     */
    public IMensaje crearMensaje(IDatosEmisor emisor, String asunto, String cuerpo, TipoMensaje tipoMensaje,ArrayList<String> usuariosReceptores, String receptorObjetivo){
        
        switch (tipoMensaje) {
        case MSJ_NORMAL: 
            return crearMensajeNormal(emisor, asunto, cuerpo, tipoMensaje, usuariosReceptores,  receptorObjetivo);
        case MSJ_CON_ALERTA: 
            return crearMensajeConAlerta(emisor, asunto, cuerpo, tipoMensaje, usuariosReceptores,  receptorObjetivo);
        case MSJ_CON_COMPROBANTE: 
            return crearMensajeConComprobante(emisor, asunto, cuerpo, tipoMensaje, usuariosReceptores,  receptorObjetivo);
        default:
            return null;
        }
    }
    
    public abstract IMensaje crearMensajeNormal(IDatosEmisor emisor, String asunto, String cuerpo, TipoMensaje tipoMensaje,ArrayList<String> usuariosReceptores, String receptorObjetivo);
    
    public abstract IMensaje crearMensajeConAlerta(IDatosEmisor emisor, String asunto, String cuerpo, TipoMensaje tipoMensaje,ArrayList<String> usuariosReceptores, String receptorObjetivo);
    
    public abstract IMensaje crearMensajeConComprobante(IDatosEmisor emisor, String asunto, String cuerpo, TipoMensaje tipoMensaje,ArrayList<String> usuariosReceptores, String receptorObjetivo);
    
}
