package emisor.modelo;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import java.util.Iterator;

import receptor.controlador.ControladorReceptor;

import receptor.modelo.Receptor;


public class Mensaje implements Serializable{
    private GregorianCalendar datetime;
    private String asunto;
    private String cuerpo;
    private Emisor emisor;
    private int id;
    private static int nextId = 0;
    
    private ArrayList<Receptor> receptores = new ArrayList<Receptor>();

    public Iterator<Receptor> getReceptores() {
        return receptores.iterator();
    }

    public Mensaje(Emisor emisor, String asunto, String cuerpo,ArrayList<Receptor> receptores) {
        this.datetime = new GregorianCalendar(); //fecha y hora actual
        this.asunto = asunto;
        this.cuerpo = cuerpo;
        this.emisor = emisor;
        this.receptores = receptores;
        this.id = nextId++;
    }

    public int getId() {
        return 0;
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Mensaje)) {
            return false;
        }
        final Mensaje other = (Mensaje) object;
        if (id != other.id) {
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

   


    private GregorianCalendar getDatetime() {
        return datetime;
    }

    private String getAsunto() {
        return asunto;
    }

    private String getCuerpo() {
        return cuerpo;
    }

    public Emisor getEmisor() {
        return emisor;
    }


    @Override
    public String toString() {
        Iterator<Receptor> itReceptores = this.getReceptores();
        StringBuilder sb = new StringBuilder();
        sb.append(this.emisor.toString());
        sb.append("\nFecha: ");
        sb.append(this.datetime.getTime());
        sb.append("\n Receptores: ");
        while(itReceptores.hasNext())
            sb.append(itReceptores.next()+",");
        
        sb.deleteCharAt(sb.length()-1);
        sb.append("\n===========================================");
        sb.append("\nAsunto: ");
        sb.append(this.getAsunto());
        sb.append("\nCuerpo: ");
        sb.append(this.getCuerpo());
        return sb.toString();
    }

    /**
     * <b>Pre:</b> datetime != null, emisor != null, asunto != null y asunto != "", cuerpo != null, cuerpo != "".
     * <b>Post:</b> el mensaje es mostrado al receptor en la vista que corresponda
     * <b>Invariante:</b> datetime, asunto, cuerpo y emisor del mensaje no var�an
     */
    public void onLlegada(){
        // el mensaje comun no hace nada
       //
    }
    
}
