package emisor.modelo;

import java.util.ArrayList;

import receptor.controlador.ControladorReceptor;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;
import receptor.modelo.SistemaReceptor;

public class MensajeConComprobante extends Mensaje {

    public MensajeConComprobante(Emisor emisor, String asunto, String cuerpo,ArrayList<Receptor> receptores) {
        super(emisor, asunto, cuerpo,receptores);
    }

    @Override
    public void onLlegada() {
        super.onLlegada();
        Comprobante comprobante = new Comprobante(this.getId(),ControladorReceptor.getInstance().getReceptor());
        ControladorReceptor.getInstance().enviarComprobante(comprobante,this.getEmisor());
    }

    @Override
    public String toString() {
        return "Mensaje con Comprobante\n"+super.toString();
    } 
}
