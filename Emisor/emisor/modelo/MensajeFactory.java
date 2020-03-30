
package emisor.modelo;

import java.util.GregorianCalendar;


public abstract class MensajeFactory {
    public enum TipoMensaje {
        MSJ_NORMAL,
        MSJ_CON_ALERTA,
        MSJ_CON_COMPROBANTE;
    }
    
    public MensajeFactory() {
        super();
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
    public static Mensaje crearMensaje(Emisor emisor, String asunto, String cuerpo, TipoMensaje tipoMensaje){
        Mensaje instance = null;
        
        switch (tipoMensaje) {
        case MSJ_NORMAL: new Mensaje(emisor, asunto, cuerpo);
            break;
        case MSJ_CON_ALERTA: new MensajeConAlerta(emisor, asunto, cuerpo);    
            break;
        case MSJ_CON_COMPROBANTE: new MensajeConComprobante(emisor, asunto, cuerpo);
            break;
        default:
        }
        
        return instance;
    }
}

