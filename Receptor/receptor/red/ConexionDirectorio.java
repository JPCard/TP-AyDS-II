package receptor.red;

public abstract class ConexionDirectorio implements Runnable {
    static final int TIEMPO_HEARTBEAT = 1500;

    public abstract void avisoPeriodicoConexion();

    @Override
    public void run(){
        this.avisoPeriodicoConexion();
    }
}
