
package emisor.modelo;

import java.util.ArrayList;


public class MensajeFactory extends AbstractMensajeFactory{

    public MensajeFactory() {
    }


    
    protected IMensaje crearMensajeNormal(IDatosEmisor emisor, String asunto, String cuerpo, TipoMensaje tipoMensaje,ArrayList<String> usuariosReceptores, String receptorObjetivo){
        return new Mensaje(emisor, asunto, cuerpo,usuariosReceptores,  receptorObjetivo);
    }
    
    protected IMensaje crearMensajeConAlerta(IDatosEmisor emisor, String asunto, String cuerpo, TipoMensaje tipoMensaje,ArrayList<String> usuariosReceptores, String receptorObjetivo){
        return new MensajeConAlerta(emisor, asunto, cuerpo,usuariosReceptores,  receptorObjetivo);  
    }
    
    protected IMensaje crearMensajeConComprobante(IDatosEmisor emisor, String asunto, String cuerpo, TipoMensaje tipoMensaje,ArrayList<String> usuariosReceptores, String receptorObjetivo){
        return new MensajeConComprobante(emisor, asunto, cuerpo,usuariosReceptores,receptorObjetivo);
    }
    
}

