package receptor.modelo;

import directorio.modelo.Directorio;

import directorio.modelo.DirectorioMain;

import java.io.FileNotFoundException;

import java.security.PrivateKey;

import receptor.persistencia.IPersistenciaReceptor;
import receptor.persistencia.PersistenciaReceptor;

import receptor.red.TCPHeartbeat;
import receptor.red.TCPdeReceptor;

public class SistemaReceptor {
    private static SistemaReceptor instance = null;
    private Receptor receptor;
    private TCPdeReceptor tcpdeReceptor;
    private IPersistenciaReceptor persistencia = new PersistenciaReceptor();
    private PrivateKey llavePrivada;
    
    private SistemaReceptor() throws FileNotFoundException {
        super();
        this.receptor = persistencia.cargarReceptor();
        this.llavePrivada = persistencia.cargarLlavePrivada();
        this.tcpdeReceptor = new TCPdeReceptor(persistencia.cargarIPServidorMensajeria(),persistencia.cargarPuertoServidorMensajeria());
    }

    public PrivateKey getLlavePrivada() {
        return llavePrivada;
    }

    public TCPdeReceptor getTcpdeReceptor() {
        return tcpdeReceptor;
    }

    

    public Receptor getReceptor() {
        return receptor;
    }


    public static void inicializar() throws FileNotFoundException {
        if (instance == null) {
            instance = new SistemaReceptor();
            Thread hilo = new Thread(instance.tcpdeReceptor);
            hilo.start();
            IPersistenciaReceptor auxPersistencia = instance.persistencia;
            Thread hiloHeartbeat =
                new Thread(new TCPHeartbeat(auxPersistencia.cargarIPDirectorio(),
                                            auxPersistencia.cargarPuertoConexion()));
            hiloHeartbeat.start();

        }



    }

    public static SistemaReceptor getInstance() {
        return instance;
    }

    public int getPuerto() {
        return this.receptor.getPuerto();
    }
}
