package receptor.red;

public abstract class ConexionDirectorio implements Runnable {
    static final int TIEMPO_HEARTBEAT = 1500;

    protected abstract void avisoPeriodicoConexion();

    @Override
    public final void run(){
        this.avisoPeriodicoConexion();
    }
}
