package directorio.modelo;

import directorio.persistencia.IPersistenciaDirectorio;

import directorio.persistencia.PersistenciaDirectorio;

import directorio.red.ReceptoresAsincronicos;

import emisor.controlador.ControladorEmisor;

import emisor.modelo.SistemaEmisor;

import java.io.IOException;
import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

import receptor.modelo.IComprobante;
import receptor.modelo.Receptor;

public class Directorio {
    private static int TIEMPO_TIMEOUT = 2000;
    private TreeMap<String, Receptor> receptores = new TreeMap<String, Receptor>();
    private HashMap<String, Long> tiempos = new HashMap<String, Long>(); // <usuarioReceptor,tiempoUltimoHearbeat>
    private Long tiempoUltModif = new GregorianCalendar().getTimeInMillis(); //en estado inicial ya esta modificado
    private IPersistenciaDirectorio persistenciaDirectorio;

    private int puertoRecibeHeartbeats;
    private int puertoRecibeGetDestinatarios;
    private int puertoRecibeGetUltimoCambio;
    private String ipServidorMensajeria;
    private int puertoPushReceptores;
    
    
    
    private String ipOtroDirectorio;
    private int otroDirectorioPuertoDestinatarios;
    private int otroDirectorioPuertoHeartbeats;
    private int otroDirectorioPuertoUltimoCambio;

    public int getPuertoRecibeHeartbeats() {
        return puertoRecibeHeartbeats;
    }

    public int getPuertoRecibeGetDestinatarios() {
        return puertoRecibeGetDestinatarios;
    }

    public int getPuertoRecibeGetUltimoCambio() {
        return puertoRecibeGetUltimoCambio;
    }

    public IPersistenciaDirectorio getPersistenciaDirectorio() {
        return persistenciaDirectorio;
    }
    private static Directorio instance = null;

    public static Directorio getInstance() {
        if (instance == null)
            instance = new Directorio();
        return instance;
    }

    private Directorio() {
        super();
        this.persistenciaDirectorio = new PersistenciaDirectorio();

        try {
            this.puertoRecibeGetDestinatarios = this.persistenciaDirectorio.cargarPuertoRecibeGetDestinatarios();
            this.puertoRecibeGetUltimoCambio = this.persistenciaDirectorio.cargarPuertoRecibeGetUltimoCambio();
            this.puertoRecibeHeartbeats = this.persistenciaDirectorio.cargarPuertoRecibeHeartbeats();
            this.ipServidorMensajeria = this.persistenciaDirectorio.cargarIPServidorMensajeria();
            this.puertoPushReceptores = this.persistenciaDirectorio.cargarpuertoPushReceptores();
            
            this.ipOtroDirectorio = this.persistenciaDirectorio.cargarIPOtroDirectorio();
            this.otroDirectorioPuertoDestinatarios = this.persistenciaDirectorio.cargarPuertoGetDestinatariosOtroDirectorio();
            this.otroDirectorioPuertoHeartbeats = this.persistenciaDirectorio.cargarPuertoHeartbeatsOtroDirectorio();
            this.otroDirectorioPuertoUltimoCambio = this.persistenciaDirectorio.cargarPuertoUltimoCambioOtroDirectorio();
            
        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    public int getOtroDirectorioPuertoUltimoCambio() {
        return otroDirectorioPuertoUltimoCambio;
    }

    public void setTiempoUltModif(Long tiempoUltModif) {
        this.tiempoUltModif = tiempoUltModif;
    }

    public String getIpServidorMensajeria() {
        return ipServidorMensajeria;
    }

    public int getPuertoPushReceptores() {
        return puertoPushReceptores;
    }

    /**
     *
     * @return Coleccion ordenada por usuario de receptores
     */
    public Collection<Receptor> getReceptores() {
        synchronized (receptores) {
            return new ArrayList(receptores.values()); //para poder serializar y mandar por red
        }
    }

    public HashMap<String, Long> getTiempos() {
        return tiempos;
    }

    /**
     * Este metodo debe llamarse cuando se agrega un nuevo receptor o cuando se modifica un receptor
     * @param receptor
     */
    public void heartbeatRecibido(Receptor receptor) {

        synchronized (receptores) {
            synchronized (tiempos) {
                this.tiempos.put(receptor.getUsuario(), GregorianCalendar.getInstance().getTimeInMillis());
            }
            if (this.receptores.containsKey(receptor.getUsuario())) {
                Receptor receptorAnt = receptores.remove(receptor.getUsuario());
                if (!receptorAnt.isConectado() || !Receptor.equalsDatosNoIdentif(receptorAnt, receptor)) {
                    if (!receptorAnt.isConectado())
                        notificarNuevoReceptor(receptor); //volvio! avisemos
                    //detecta si alguien cambia de estado de conexion (de desconectado a conectado), IP, puerto o nombre
                    receptor.setConectado(true); //acaba de llegar por lo que esta conectado
                    this.receptores.put(receptor.getUsuario(), receptor);
                    this.updateTiempoUltModif();
                } else
                    this.receptores.put(receptorAnt.getUsuario(), receptorAnt); //como no habia cambiado lo devuelvo
            } else {
                notificarNuevoReceptor(receptor); //nuevo, no vino antes
                receptor.setConectado(true); //acaba de llegar por lo que esta conectado
                this.receptores.put(receptor.getUsuario(), receptor);
                this.updateTiempoUltModif();
            }

        }


        // lo metes al treemap y pones el tiempo actual en tiempos

    }

    private void notificarNuevoReceptor(Receptor receptor) {
//        System.out.println("apache");
        ReceptoresAsincronicos.avisarReceptorSeConecto(receptor);
    }


    public Collection<Receptor> listaDestinatariosRegistrados() {
        System.out.println("==============================A");
        Long tiempoActual = GregorianCalendar.getInstance().getTimeInMillis();
        System.out.println("==============================B");
        boolean alguienSeFue = false;
        System.out.println("==============================C");

        synchronized (receptores) { //para que no puedan cambiar los tiempos mientras se evaluan los online de cada receptor
        System.out.println("==============================D");
            synchronized (tiempos) {
                System.out.println("LLEGAMOS EEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
                for (Object obj : receptores.values()) {
                    Receptor receptor = (Receptor) obj;
                    boolean online;

                    online = (tiempoActual - this.tiempos.get(receptor.getUsuario())) <= TIEMPO_TIMEOUT;
                    if (!alguienSeFue && receptor.isConectado() && !online) // se fue alguien
                    {
                        alguienSeFue = true;
                    }

                    receptor.setConectado(online);

                }
            }

        }

        if (alguienSeFue)
            this.updateTiempoUltModif();


        return this.getReceptores();
    }


    public long getTiempoUltModif() {
        synchronized (tiempoUltModif) {
            return this.tiempoUltModif;
        }
    }

    private void updateTiempoUltModif() {
        synchronized (tiempoUltModif) {
            this.tiempoUltModif = new GregorianCalendar().getTimeInMillis();
        }
    }


    public String getIpOtroDirectorio() {
        return ipOtroDirectorio;
    }

    public int getOtroDirectorioPuertoDestinatarios() {
        return otroDirectorioPuertoDestinatarios;
    }

    public int getOtroDirectorioPuertoHeartbeats() {
        return otroDirectorioPuertoHeartbeats;
    }

    public void setReceptores(TreeMap<String, Receptor> receptores) {
        synchronized(receptores){
            this.receptores = receptores;
        }
        
    }

}
