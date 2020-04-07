package directorio.modelo;

import directorio.red.DestinatariosRegistradosThread;
import directorio.red.HeartbeatThread;

public class DirectorioMain {
    public static int HEARTBEAT_PORT = 27444;
    public static int GETDESTINATARIOS_PORT = 27445;
    public static String DIRECTORIO_IP = "127.0.0.1";
    public DirectorioMain() {
        super();
    }

    public static void main(String[] args) {
        Directorio directorio = new Directorio();
        System.out.println("Directorio creado");
        HeartbeatThread heartbeat = new HeartbeatThread(directorio,HEARTBEAT_PORT);
        heartbeat.start();
        
        DestinatariosRegistradosThread destinatariosRegistrados = new DestinatariosRegistradosThread(directorio,GETDESTINATARIOS_PORT);
        destinatariosRegistrados.start();
        
        
    }
}
