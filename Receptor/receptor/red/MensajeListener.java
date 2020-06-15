package receptor.red;

public abstract class MensajeListener implements Runnable {
    
    public abstract void escucharMensajes();
    
    @Override
   public void run(){
        this.escucharMensajes();
    }
}
