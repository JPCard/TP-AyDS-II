package emisor.modelo;

import java.util.GregorianCalendar;

import receptor.controlador.ControladorReceptor;


public class Mensaje {
    private GregorianCalendar datetime;
    private String asunto;
    private String cuerpo;
    private Emisor emisor;


    public Mensaje(Emisor emisor, String asunto, String cuerpo) {
        this.datetime = new GregorianCalendar(); //fecha y hora actual
        this.asunto = asunto;
        this.cuerpo = cuerpo;
        this.emisor = emisor;
    }


    private GregorianCalendar getDatetime() {
        return datetime;
    }

    private String getAsunto() {
        return asunto;
    }

    private String getCuerpo() {
        return cuerpo;
    }

    private Emisor getEmisor() {
        return emisor;
    }


    /**
     * <b>Pre:</b> datetime != null, emisor != null, asunto != null y asunto != "", cuerpo != null, cuerpo != "".
     * <b>Post:</b> el mensaje es mostrado al receptor en la vista que corresponda
     * <b>Invariante:</b> datetime, asunto, cuerpo y emisor del mensaje no varían
     */
    public void onLlegada(){
        //ControladorReceptor.getInstance().muestraMensaje() TODO
    }
    
}
