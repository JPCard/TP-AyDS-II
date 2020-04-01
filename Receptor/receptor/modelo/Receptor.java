package receptor.modelo;



import java.io.Serializable;


public class Receptor implements Serializable{
    private String IP;
    private int puerto;
    private String nombre;


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Receptor)) {
            return false;
        }
        final Receptor other = (Receptor) object;
        if (!(IP == null ? other.IP == null : IP.equals(other.IP))) {
            return false;
        }
        if (puerto != other.puerto) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((IP == null) ? 0 : IP.hashCode());
        result = PRIME * result + puerto;
        return result;
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

}
