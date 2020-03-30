package emisor.modelo;

import java.util.GregorianCalendar;

public class MensajeConComprobante extends Mensaje {

    public MensajeConComprobante(Emisor emisor, String asunto, String cuerpo) {
        super(emisor, asunto, cuerpo);
    }

    @Override
    public void onLlegada() {
        //TODO
        super.onLlegada();
    }
}
