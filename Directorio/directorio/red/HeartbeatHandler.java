package directorio.red;

import directorio.modelo.Directorio;

import receptor.modelo.Heartbeat;

public class HeartbeatHandler implements Runnable{
    
    private Heartbeat heartbeat;
    public HeartbeatHandler(Heartbeat heartbeat) {
        super();
        this.heartbeat = heartbeat;
    }

    @Override
    public void run() {
        Directorio.getInstance().heartbeatRecibido(heartbeat.getReceptor());
    }
}
