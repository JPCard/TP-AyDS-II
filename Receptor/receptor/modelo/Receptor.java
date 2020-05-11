package receptor.modelo;


import directorio.modelo.Directorio;

import java.io.Serializable;


public class Receptor implements Serializable,Comparable<Receptor>{
    private String IP;
    private int puerto;
    private String nombre;
    private int ID = Directorio.INVALID_ID;
    private boolean conectado = true; //administrado por el directorio
    private String usuario;


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Receptor)) {
            return false;
        }
        final Receptor other = (Receptor) object;
        if (!(usuario == null ? other.usuario == null : usuario.equals(other.usuario))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((usuario == null) ? 0 : usuario.hashCode());
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

    public Receptor(String IP, int puerto, String nombre,String usuario) {
        this.IP = IP;
        this.puerto = puerto;
        this.nombre = nombre;
        this.usuario = usuario;
    }

    public Receptor(){
        super();
    }


    public String getUsuario() {
        return usuario;
    }

    @Override
    public String toString() {
        return this.getUsuario();
    }

    public String descripcionCompleta(){
        return "Usuario: "+this.getUsuario()+"\nNombre: "+this.getNombre()+"\nIP: "+this.getIP()+"\nPuerto: "+this.getPuerto();
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
