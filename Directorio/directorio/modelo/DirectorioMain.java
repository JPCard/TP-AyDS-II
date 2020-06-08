package directorio.modelo;

import directorio.red.DestinatariosRegistradosThread;
import directorio.red.HeartbeatThread;
import directorio.red.UltimoCambioThread;

public class DirectorioMain {
    public DirectorioMain() {
        super();
    }

    public static void main(String[] args) {
        Directorio.getInstance();
        System.out.println("Directorio creado");
        
        
        //este estaba segundo pero lo puse primero
        DestinatariosRegistradosThread destinatariosRegistrados = new DestinatariosRegistradosThread(Directorio.getInstance());
        destinatariosRegistrados.start();
        
        HeartbeatThread heartbeat = new HeartbeatThread(Directorio.getInstance());
        heartbeat.start();
        
        
        
        
        
        
        UltimoCambioThread ultimoCambioThread = new UltimoCambioThread(Directorio.getInstance());
        ultimoCambioThread.start();

        
        
    }
}
