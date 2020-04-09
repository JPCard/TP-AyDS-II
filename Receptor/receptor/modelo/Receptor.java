package receptor.modelo;


import directorio.modelo.Directorio;

import java.io.Serializable;


public class Receptor implements Serializable,Comparable<Receptor>{
    private String IP;
    private int puerto;
    private String nombre;
    private int ID = Directorio.INVALID_ID;
    private boolean conectado = true; //administrado por el directorio


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Receptor)) {
            return false;
        }
        final Receptor other = (Receptor) object;
        if (ID != other.ID) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ID;
        return result;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

    public boolean isConectado() {
        return conectado;
    }

    public int getID() {
        return ID;
    }

    public Receptor(String IP, int puerto, String nombre) {
        this.IP = IP;
        this.puerto = puerto;
        this.nombre = nombre;
    }

    public Receptor(){
        super();
    }

    @Override
    public String toString() {
        return this.getNombre();
    }

    public String descripcionCompleta(){
        return "Nombre: "+this.getNombre()+"\nIP: "+this.getIP()+"\nPuerto: "+this.getPuerto();
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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


    @Override
    public int compareTo(Receptor receptor) {
        return this.getNombre().compareTo(receptor.getNombre());
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }
}
