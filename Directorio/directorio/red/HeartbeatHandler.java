package directorio.red;

import directorio.modelo.IDirectorio;

import directorio.modelo.IDirectorio;

import receptor.modelo.Heartbeat;

public class HeartbeatHandler implements Runnable{
    private IDirectorio directorio;
    private Heartbeat heartbeat;


    public HeartbeatHandler(IDirectorio directorio, Heartbeat heartbeat) {
        this.directorio = directorio;
        this.heartbeat = heartbeat;
    }

    @Override
    public void run() {
        directorio.heartbeatRecibido(heartbeat.getReceptor());
    }
}
