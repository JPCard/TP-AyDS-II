package emisor.modelo;

import java.util.GregorianCalendar;

public class MensajeConAlerta extends Mensaje {

    public MensajeConAlerta(Emisor emisor, String asunto, String cuerpo) {
        super(emisor, asunto, cuerpo);
    }


    @Override
    public void onLlegada() {
        // TODO Implement this method
        super.onLlegada();
    }
}
