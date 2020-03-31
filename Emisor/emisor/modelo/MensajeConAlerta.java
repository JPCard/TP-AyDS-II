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
        // TODO Implement this method
        super.onLlegada();
        ControladorReceptor.getInstance().activarAlerta();
        System.out.println("ASDSADJHASVF");
    }
}
