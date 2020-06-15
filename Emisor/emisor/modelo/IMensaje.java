package emisor.modelo;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;

public interface IMensaje extends Cloneable, Serializable {
    
    public IMensaje clone();

    public String getReceptorObjetivo();

    public Iterator<String> getReceptores();

    public void setReceptorObjetivo(String receptorObjetivo);

    public int getId();

    public void setId(int id);

    @Override
    public boolean equals(Object object);

    @Override
    public int hashCode();

    public GregorianCalendar getDatetime();

    public String getAsunto();

    public String getCuerpo();

    public Emisor getEmisor();

    @Override
    public String toString();

    public void setDatetime(GregorianCalendar datetime);

    public void setAsunto(String asunto);

    public void setCuerpo(String cuerpo);

    public void setEmisor(Emisor emisor);

    public void setUsuariosReceptores(ArrayList<String> usuariosReceptores);

    public ArrayList<String> getUsuariosReceptores();

    /**
     * <b>Pre:</b> datetime != null, emisor != null, asunto != null y asunto != "", cuerpo != null, cuerpo != "".
     * <b>Post:</b> el mensaje es mostrado al receptor en la vista que corresponda
     * <b>Invariante:</b> datetime, asunto, cuerpo y emisor del mensaje no varían
     */
    public void onLlegada();
}
