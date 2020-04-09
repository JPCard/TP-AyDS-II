package receptor.modelo;

import directorio.modelo.Directorio;

import directorio.modelo.DirectorioMain;

import java.io.FileNotFoundException;

import receptor.persistencia.IPersistenciaReceptor;
import receptor.persistencia.PersistenciaReceptor;

import receptor.red.TCPHeartbeat;
import receptor.red.TCPdeReceptor;

public class SistemaReceptor {
    private static SistemaReceptor instance = null;
    private Receptor receptor;
    private TCPdeReceptor tcpdeReceptor = new TCPdeReceptor();
    private IPersistenciaReceptor persistencia = new PersistenciaReceptor();

    public TCPdeReceptor getTcpdeReceptor() {
        return tcpdeReceptor;
    }

    private SistemaReceptor() throws FileNotFoundException {
        super();
        this.receptor = persistencia.cargarReceptor();

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
                                            auxPersistencia.cargarPuertoHeartbeat(),
                                            auxPersistencia.cargarPuertoRegistro()));
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
