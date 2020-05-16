package emisor.modelo;

import java.io.Serializable;

import java.util.ArrayList;

import receptor.controlador.ControladorReceptor;

import receptor.modelo.Receptor;

public class MensajeConAlerta extends Mensaje implements Serializable {

    public MensajeConAlerta(Emisor emisor, String asunto, String cuerpo,ArrayList<String> receptores) {
        super(emisor, asunto, cuerpo,receptores);
    }
    
    public MensajeConAlerta(){
        super();
    } //para serializacion

    @Override
    public void onLlegada() {
        super.onLlegada();
        ControladorReceptor.getInstance().activarAlerta();
    }


    @Override
    public String toString() {
        return "Mensaje con Alerta\n"+super.toString();
    }
}
