package emisor.modelo;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import java.util.Iterator;


import receptor.modelo.IDatosReceptor;
import receptor.modelo.ILlegadaMensaje;


public class Mensaje implements IMensaje {
    private GregorianCalendar datetime;
    private String asunto;
    private String cuerpo;
    private IDatosEmisor emisor;
    private int id;
    private ArrayList<String> usuariosReceptores = new ArrayList<String>();
    private String receptorObjetivo;
    
    public Mensaje(IDatosEmisor emisor, String asunto, String cuerpo, ArrayList<String> usuariosReceptores,
                   String receptorObjetivo) {
        this.datetime = new GregorianCalendar(); //fecha y hora actual
        this.asunto = asunto;
        this.cuerpo = cuerpo;
        this.emisor = emisor;
        this.receptorObjetivo = receptorObjetivo;

        this.usuariosReceptores = usuariosReceptores;
        this.id = -1;
    }

   

    public Mensaje() { //para la serializacion
        super();
    }


    @Override
    public IMensaje clone() {
        IMensaje m = MensajeFactory.getInstance()
               .crearMensaje(this.getEmisor(), this.getAsunto(), this.getCuerpo(),
                             AbstractMensajeFactory.TipoMensaje.MSJ_NORMAL, this.getUsuariosReceptores(),
                             this.getReceptorObjetivo());
        m.setId(this.getId());
        return m;
    }

    public String getReceptorObjetivo() {
        return receptorObjetivo;
    }


    public Iterator<String> getReceptores() {
        return usuariosReceptores.iterator();
    }



    public void setReceptorObjetivo(String receptorObjetivo) {
        this.receptorObjetivo = receptorObjetivo;
    }
    
    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof IMensaje)) {
            return false;
        }
        final IMensaje other = (IMensaje) object;
        if (id != other.getId()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + id;
        return result;
    }


    public GregorianCalendar getDatetime() {
        return datetime;
    }

    public String getAsunto() {
        return asunto;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public IDatosEmisor getEmisor() {
        return emisor;
    }


    @Override
    public String toString() {
        Iterator<String> itReceptores = this.getReceptores();
        StringBuilder sb = new StringBuilder();
        sb.append(this.emisor.toString());
        sb.append("\nFecha: ");
        sb.append(this.datetime.getTime());
        sb.append("\n Receptores: ");
        while (itReceptores.hasNext())
            sb.append(itReceptores.next() + ",");

        sb.deleteCharAt(sb.length() - 1);
        sb.append("\n===========================================");
        sb.append("\nAsunto: ");
        sb.append(this.getAsunto());
        sb.append("\nCuerpo: ");
        sb.append(this.getCuerpo());
        return sb.toString();
    }


    public void setDatetime(GregorianCalendar datetime) {
        this.datetime = datetime;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public void setEmisor(IDatosEmisor emisor) {
        this.emisor = emisor;
    }

    public void setUsuariosReceptores(ArrayList<String> usuariosReceptores) {
        this.usuariosReceptores = usuariosReceptores;
    }

    public ArrayList<String> getUsuariosReceptores() {
        return usuariosReceptores;
    }


    /**
     * <b>Pre:</b> datetime != null, emisor != null, asunto != null y asunto != "", cuerpo != null, cuerpo != "".
     * <b>Post:</b> el mensaje es mostrado al receptor en la vista que corresponda
     * <b>Invariante:</b> datetime, asunto, cuerpo y emisor del mensaje no var�an
     */


    @Override
    public void onLlegada(ILlegadaMensaje llegadaMensaje) {
        llegadaMensaje.arriboMensajeSimple(this);
    }
}
