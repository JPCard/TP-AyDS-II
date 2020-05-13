package receptor.modelo;


import directorio.modelo.Directorio;

import java.io.Serializable;


public class Receptor implements Serializable, Comparable<Receptor> {
    private String IP;
    private int puerto;
    private String nombre;
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

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }


    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

    public boolean isConectado() {
        return conectado;
    }

    public Receptor(String IP, int puerto, String nombre, String usuario) {
        this.IP = IP;
        this.puerto = puerto;
        this.nombre = nombre;
        this.usuario = usuario;
    }

    public Receptor() {
        super();
    }


    public String getUsuario() {
        return usuario;
    }

    @Override
    public String toString() {
        return this.getUsuario();
    }

    public String descripcionCompleta() {
        return "Usuario: " + this.getUsuario() + "\nNombre: " + this.getNombre() + "\nIP: " + this.getIP() +
               "\nPuerto: " + this.getPuerto();
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
        return this.getUsuario().compareTo(receptor.getUsuario());
    }

    /**
     * Pre: receptor1 != null, receptor2 != null
     * @param receptor1
     * @param receptor2
     * @return el estado de verdad de la afirmacion de que receptor1 y receptor2 tienen los mismos datos no identificatorios
     */
    public static boolean equalsDatosNoIdentif(Receptor receptor1, Receptor receptor2) {
        return (receptor1.getIP()).equals(receptor2.getIP()) && (receptor1.getPuerto() == receptor2.getPuerto()) &&
               (receptor1.getNombre()).equals(receptor2.getNombre());
    }

}
