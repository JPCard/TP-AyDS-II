package emisor.modelo;


import java.io.Serializable;

import java.util.Iterator;

import receptor.modelo.IDatosReceptor;

public class DatosEmisor implements IDatosEmisor {
    private String IP; //{id}
    private int puerto;
    private String nombre;
    private transient Agenda agenda;

    public DatosEmisor() {
        super();
    }
    
    public DatosEmisor(int puerto, String IP, String nombre) {
        this.IP = IP;
        this.puerto = puerto;
        this.nombre = nombre;
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
       return new DatosEmisor(this.getPuerto(),this.getIP(),this.getNombre());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof IDatosEmisor)) {
            return false;
        }
        final IDatosEmisor other = (IDatosEmisor) object;
        if (!(nombre == null ? other.getNombre() == null : nombre.equals(other.getNombre()))) {
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

    


    public Iterator<IDatosReceptor> consultarAgenda() {
        return this.agenda.getIteratorContactos();
    }
    
    
    
}
