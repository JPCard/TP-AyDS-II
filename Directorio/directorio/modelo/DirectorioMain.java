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
        HeartbeatThread heartbeat = new HeartbeatThread(Directorio.getInstance());
        heartbeat.start();
        
        
        
        DestinatariosRegistradosThread destinatariosRegistrados = new DestinatariosRegistradosThread(Directorio.getInstance());
        destinatariosRegistrados.start();
        
        
        UltimoCambioThread ultimoCambioThread = new UltimoCambioThread(Directorio.getInstance());
        ultimoCambioThread.start();

        
        
    }
}
