package receptor.modelo;

public class Receptor {
    private String IP;
    private int puerto;
    private String nombre;
    private static Receptor instance;

    public Receptor() {
        super();
    }

    public Receptor(String IP, int puerto, String nombre) {
        this.IP = IP;
        this.puerto = puerto;
        this.nombre = nombre;
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

    public static void setInstance(Receptor instance) {
        Receptor.instance = instance;
    }

    public static Receptor getInstance(){
        return instance;
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
