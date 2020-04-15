package directorio.modelo;

import directorio.red.DestinatariosRegistradosThread;
import directorio.red.HeartbeatThread;
import directorio.red.RegistroThread;

public class DirectorioMain {
    public static final int HEARTBEAT_PORT = 27444;
    public static final int GETDESTINATARIOS_PORT = 27445;
    public static final int REGISTRO_PORT = 1234;
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
        
        RegistroThread registroThread = new RegistroThread(REGISTRO_PORT);
        registroThread.start();
        

        
        
    }
}
