package emisor.modelo;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import receptor.controlador.ControladorReceptor;

import receptor.modelo.Receptor;

public class MensajeConAlerta extends Mensaje {

    public MensajeConAlerta(Emisor emisor, String asunto, String cuerpo,ArrayList<Receptor> receptores) {
        super(emisor, asunto, cuerpo,receptores);
    }


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
