
package emisor.modelo;

import java.util.ArrayList;


public class MensajeFactory extends AbstractMensajeFactory{
    private static AbstractMensajeFactory instance = null;

    private MensajeFactory() {
    }

    public static AbstractMensajeFactory getInstance(){
        if(instance == null)
            instance = new MensajeFactory();
        return instance;
    }
    
    public IMensaje crearMensajeNormal(IDatosEmisor emisor, String asunto, String cuerpo, TipoMensaje tipoMensaje,ArrayList<String> usuariosReceptores, String receptorObjetivo){
        return new Mensaje(emisor, asunto, cuerpo,usuariosReceptores,  receptorObjetivo);
    }
    
    public IMensaje crearMensajeConAlerta(IDatosEmisor emisor, String asunto, String cuerpo, TipoMensaje tipoMensaje,ArrayList<String> usuariosReceptores, String receptorObjetivo){
        return new MensajeConAlerta(emisor, asunto, cuerpo,usuariosReceptores,  receptorObjetivo);  
    }
    
    public IMensaje crearMensajeConComprobante(IDatosEmisor emisor, String asunto, String cuerpo, TipoMensaje tipoMensaje,ArrayList<String> usuariosReceptores, String receptorObjetivo){
        return new MensajeConComprobante(emisor, asunto, cuerpo,usuariosReceptores,receptorObjetivo);
    }
    
}

