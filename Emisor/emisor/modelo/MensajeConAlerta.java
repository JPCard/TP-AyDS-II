package emisor.modelo;

import java.io.Serializable;

import java.util.ArrayList;

import receptor.controlador.ControladorReceptor;

import receptor.modelo.IDatosReceptor;
import receptor.modelo.ILlegadaMensaje;

public class MensajeConAlerta extends Mensaje implements Serializable {

    public MensajeConAlerta(IDatosEmisor emisor, String asunto, String cuerpo, ArrayList<String> receptores,
                            String receptorObjetivo) {
        super(emisor, asunto, cuerpo, receptores, receptorObjetivo);
    }

    public MensajeConAlerta() {
        super();
    } //para serializacion

    @Override
    public void onLlegada(ILlegadaMensaje llegadaMensaje) {
        
        super.onLlegada(llegadaMensaje);
        llegadaMensaje.arriboMensajeConAlerta(this);
    }


    @Override
    public IMensaje clone() {
        IMensaje m = MensajeFactory.getInstance()
               .crearMensaje(this.getEmisor(), this.getAsunto(), this.getCuerpo(),
                             AbstractMensajeFactory.TipoMensaje.MSJ_CON_ALERTA, this.getUsuariosReceptores(),
                             this.getReceptorObjetivo());
        m.setId(this.getId());
        return m;
    }

    @Override
    public String toString() {
        return "IMensaje con Alerta\n" + super.toString();
    }
}
