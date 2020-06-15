package receptor.red;

public abstract class MensajeListener implements Runnable {
    
    protected abstract void escucharMensajes();
    
    @Override
   public  final void run(){
        this.escucharMensajes();
    }
}
