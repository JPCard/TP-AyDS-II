package servidormensajeria.modelo;

import emisor.modelo.Mensaje;

import java.util.ArrayList;

import receptor.modelo.Receptor;

import servidormensajeria.persistencia.IPersistenciaServidor;

import servidormensajeria.red.TCPParaDirectorio;
import servidormensajeria.red.ComprobanteListener;
import servidormensajeria.red.MensajeListener;

public class SistemaServidor {
    private static SistemaServidor instance;
    private IPersistenciaServidor persistenciaServidor;
//    private MensajeListener mensajeListener;
//    private ComprobanteListener comprobanteListener;
    private TCPParaDirectorio tcpParaDirectorio;
    
    //cosas para saber donde estna los usuarios
    private ArrayList<Receptor> receptores = null;
    private long tiempoUltimaActualizacionReceptores = -1;
    
    public final static int PUERTO_MENSAJES = 27446;
    public final static int PUERTO_COMPROBANTES = 27447;
    
    public final static String IP_DIRECTORIO = "127.0.0.1";
    public final static int PUERTO_DIRECTORIO_DESTINATARIOS = 27445; //TODO ESTO NO DEBERIA SER STATIC
    public final static int PUERTO_DIRECTORIO_TIEMPO = 1; //TODO ESTO NO DEBERIA SER STATIC

    public static void main(String[] args) {
        getInstance();
        //la linea anterior hay que cargar del archivo el metodo de persistencia
        //getInstance().persistenciaServidor; TODO todavia no hay
        getInstance().tcpParaDirectorio = new TCPParaDirectorio(IP_DIRECTORIO,PUERTO_DIRECTORIO_DESTINATARIOS,PUERTO_DIRECTORIO_TIEMPO);
        new Thread(new MensajeListener()).start();
        System.out.println("hola");
        new Thread(new ComprobanteListener()).start();
        
    }


    public long getTiempoUltimaActualizacionReceptores() {
        return tiempoUltimaActualizacionReceptores;
    }

    public void setReceptores(ArrayList<Receptor> receptores) {
        this.receptores = receptores;
    }

    public ArrayList<Receptor> getReceptores() {
        return receptores;
    }

    public void setTiempoUltimaActualizacionReceptores(long tiempoUltimaActualizacionReceptores) {
        this.tiempoUltimaActualizacionReceptores = tiempoUltimaActualizacionReceptores;
    }

    private SistemaServidor() {
        super();
    }

    public static SistemaServidor getInstance(){
        if(instance == null)
            instance = new SistemaServidor();
        return instance;
    }
    
    public boolean isReceptorConectado(String username){
        return true; //todo
    }
    
    public void arriboMensaje(Mensaje mensaje){
            new Thread(new MensajeHandler(mensaje)).start();
    }

    public IPersistenciaServidor getPersistencia() {
        return this.persistenciaServidor;
    }

    public TCPParaDirectorio getDirectorio() {
        return this.tcpParaDirectorio;
    }

}
