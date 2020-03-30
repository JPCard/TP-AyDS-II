package receptor.modelo;

public class Receptor {
    private String IP;
    private int puerto;
    private String nombre;

    public Receptor(String IP, int puerto, String nombre) {
        this.IP = IP;
        this.puerto = puerto;
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
