
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
    
    public Mensaje crearMensajeNormal(Emisor emisor, String asunto, String cuerpo, TipoMensaje tipoMensaje,ArrayList<String> usuariosReceptores){
        return new Mensaje(emisor, asunto, cuerpo,usuariosReceptores);
    }
    
    public Mensaje crearMensajeConAlerta(Emisor emisor, String asunto, String cuerpo, TipoMensaje tipoMensaje,ArrayList<String> usuariosReceptores){
        return new MensajeConAlerta(emisor, asunto, cuerpo,usuariosReceptores);  
    }
    
    public Mensaje crearMensajeConComprobante(Emisor emisor, String asunto, String cuerpo, TipoMensaje tipoMensaje,ArrayList<String> usuariosReceptores){
        return new MensajeConComprobante(emisor, asunto, cuerpo,usuariosReceptores);
    }
    
}

