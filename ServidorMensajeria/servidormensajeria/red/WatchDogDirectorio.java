package servidormensajeria.red;

import emisor.modelo.IMensaje;

import java.io.IOException;
import java.io.ObjectInputStream;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.Collection;

import receptor.modelo.Receptor;

import servidormensajeria.modelo.ISistemaServidor;
import servidormensajeria.modelo.SistemaServidor;

public class WatchDogDirectorio implements Runnable{
    
    private String ipDirectorioPrincipal;
    private int puertoDirectorioPrincipalPushReceptores;
    
    private String ipDirectorioSecundario;
    private int puertoDirectorioSecundarioPushReceptores;
    
    
    private String ipDirectorioActual;
    private int puertoDirectorioPushReceptoresActual;
    
    private boolean usandoDirSecundario;
    
    private ISistemaServidor sistemaServidor;

    public WatchDogDirectorio(ISistemaServidor sistemaServidor,String ipDirectorioPrincipal, int puertoDirectorioPrincipalPushReceptores,
                              String ipDirectorioSecundario, int puertoDirectorioSecundarioPushReceptores) {
        this.sistemaServidor = sistemaServidor;
        this.ipDirectorioPrincipal = ipDirectorioPrincipal;
        this.puertoDirectorioPrincipalPushReceptores = puertoDirectorioPrincipalPushReceptores;
        this.ipDirectorioSecundario = ipDirectorioSecundario;
        this.puertoDirectorioSecundarioPushReceptores = puertoDirectorioSecundarioPushReceptores;
    }


    @Override
    public void run() {        
        while (true) {  
            try (ServerSocket s = new ServerSocket(this.puertoDirectorioPushReceptoresActual)) {
            s.setSoTimeout(1000);

                while (true) {
                    System.out.println("Hilo notifica sistema de mensajes: esperando");
                    try (Socket socket = s.accept()) {
                        System.out.println("Nuevo receptor: toca enviarle mensajes q le faltan");

                        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                            Receptor receptor = (Receptor) in.readObject();
                            sistemaServidor.envioMensajesAsincronicos(receptor);
                        } catch (Exception e) {
                            System.out.println("mmm!");
                            e.printStackTrace();
                        }
                    }


                }

            } catch (IOException e) {
               // e.printStackTrace();
                this.cambiarDirectorioActivo();
            } catch (Exception e) {
                //e.printStackTrace();
                this.cambiarDirectorioActivo();
            }

        }
    }

   
    


    private void cambiarDirectorioActivo(){
            System.out.println("----------------------------------------------------------cambie de directorio");
            if(this.usandoDirSecundario){
                this.ipDirectorioActual = this.ipDirectorioPrincipal;
                this.puertoDirectorioPushReceptoresActual = this.puertoDirectorioPrincipalPushReceptores;
            }
            else {
                
                this.ipDirectorioActual = this.ipDirectorioSecundario;
                this.puertoDirectorioPushReceptoresActual = this.puertoDirectorioSecundarioPushReceptores;
            }
            this.usandoDirSecundario = !this.usandoDirSecundario;
        
    }
}
