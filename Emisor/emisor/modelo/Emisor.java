package emisor.modelo;


import emisor.modelo.Agenda;

import java.io.Serializable;

import java.util.Iterator;

import receptor.modelo.Receptor;

public class Emisor implements Serializable,Cloneable{
    private String IP; //{id}
    private int puerto;
    private String nombre;
    private transient Agenda agenda;

    public Emisor() {
        super();
    }
    
    public Emisor(int puerto, String IP, String nombre) {
        this.IP = IP;
        this.puerto = puerto;
        this.nombre = nombre;
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
       return new Emisor(this.getPuerto(),this.getIP(),this.getNombre());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Emisor)) {
            return false;
        }
        final Emisor other = (Emisor) object;
        if (!(nombre == null ? other.nombre == null : nombre.equals(other.nombre))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((nombre == null) ? 0 : nombre.hashCode());
        return result;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    @Override
    public String toString() {
        return "Emisor: "+this.getNombre()+"\nIP: "+this.getIP()+"\nPuerto: "+this.getPuerto();
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public Agenda getAgenda() {
        return agenda;
    }

   

    public String getIP() {
        return IP;
    }

    public int getPuerto() {
        return puerto;
    }

    public String getNombre() {
        return nombre;
    }

    


    public Iterator<Receptor> consultarAgenda() {
        return this.agenda.getIteratorContactos();
    }
    
    
    
}
