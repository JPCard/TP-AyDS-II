package emisor.modelo;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import receptor.controlador.ControladorReceptor;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

public class MensajeConComprobante extends Mensaje {

    public MensajeConComprobante(Emisor emisor, String asunto, String cuerpo,ArrayList<Receptor> receptores) {
        super(emisor, asunto, cuerpo,receptores);
    }

    @Override
    public void onLlegada() {
        super.onLlegada();
        Comprobante comprobante = new Comprobante(this.getId(),Receptor.getInstance());
        ControladorReceptor.getInstance().enviarComprobante(comprobante,this.getEmisor());
    }

    
}
