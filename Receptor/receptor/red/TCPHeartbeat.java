package receptor.red;

import directorio.modelo.IDirectorio;



import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;

import java.net.InetSocketAddress;
import java.net.Socket;


import receptor.controlador.ControladorReceptor;

import receptor.modelo.Heartbeat;
import receptor.modelo.IDatosReceptor;
import receptor.modelo.ISistemaReceptor;
import receptor.modelo.SistemaReceptor;

public class TCPHeartbeat implements IConexionDirectorio {
    private String IPDirectorioPrincipal;
    private int puertoHeartbeatPrincipal; // en MS
    
    private String IPDirectorioSecundario;
    private int puertoHeartbeatSecundario;
    
    private boolean usandoDirSecundario;
    
    private String IPDirectorioActual;
    private int puertoHeartbeatActual;

    private ISistemaReceptor sistemaReceptor;

    public TCPHeartbeat(ISistemaReceptor sistemaReceptor,String IPDirectorioPrincipal, int puertoHeartbeatPrincipal, String IPDirectorioSecundario,
                        int puertoHeartbeatSecundario) {
        
        this.sistemaReceptor = sistemaReceptor;
        
        this.IPDirectorioPrincipal = IPDirectorioPrincipal;
        this.puertoHeartbeatPrincipal = puertoHeartbeatPrincipal;
        this.IPDirectorioSecundario = IPDirectorioSecundario;
        this.puertoHeartbeatSecundario = puertoHeartbeatSecundario;
        
        this.usandoDirSecundario = true;
        this.cambiarDirectorioActivo();
        
        
    }

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }


        while (true) {
            try {

                while (true) {
                    IDatosReceptor receptor = sistemaReceptor.getReceptor();
                    Heartbeat heartbeat = new Heartbeat(receptor);
                    
                    Socket socket = new Socket();
                    InetSocketAddress addr = new InetSocketAddress(IPDirectorioActual, this.puertoHeartbeatActual);
                    socket.connect(addr, 500);

                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    if(socket.isConnected())
                        out.writeObject(heartbeat);
                    out.close();
                    
                    socket.close();
                    ControladorReceptor.getInstance().updateConectado(true);
                    Thread.sleep(TIEMPO_HEARTBEAT);
                }


            } catch (Exception e) {
                //e.printStackTrace();
                this.cambiarDirectorioActivo();
                ControladorReceptor.getInstance().updateConectado(false);
            }
        }

    }
    
    private void cambiarDirectorioActivo(){
        if(this.usandoDirSecundario){
            this.IPDirectorioActual = this.IPDirectorioPrincipal;
            this.puertoHeartbeatActual = this.puertoHeartbeatPrincipal;
            
            
        }
        else {
            this.IPDirectorioActual = this.IPDirectorioSecundario;
            this.puertoHeartbeatActual = this.puertoHeartbeatSecundario;
            
        }
        this.usandoDirSecundario = !this.usandoDirSecundario;
    }
    
}
