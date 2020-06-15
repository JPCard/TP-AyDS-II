package directorio.modelo;

import directorio.red.DestinatariosRegistradosThread;
import directorio.red.HeartbeatListener;
import directorio.red.SincronizacionInicialDirectorios;
import directorio.red.UltimoCambioThread;

public class DirectorioMain {
    public DirectorioMain() {
        super();
    }

    public static void main(String[] args) {
        IDirectorio directorio = new Directorio();
        System.out.println("IDirectorio creado");
        
        new SincronizacionInicialDirectorios(directorio).cargarListaDestinatariosRegistrados();
        System.out.println("Sincronizacion con el otro directorio completada");
        //este estaba segundo pero lo puse primero
        DestinatariosRegistradosThread destinatariosRegistrados = new DestinatariosRegistradosThread(directorio);
        destinatariosRegistrados.start();
        
        HeartbeatListener heartbeat = new HeartbeatListener(directorio);
        heartbeat.start();
        
        
        
        
        
        
        UltimoCambioThread ultimoCambioThread = new UltimoCambioThread(directorio);
        ultimoCambioThread.start();

        
        
    }
}
