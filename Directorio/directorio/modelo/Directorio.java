package directorio.modelo;

import emisor.controlador.ControladorEmisor;

import emisor.modelo.SistemaEmisor;

import java.io.IOException;
import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TreeSet;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

public class Directorio {
    private static int TIEMPO_TIMEOUT = 500;
    public static final int INVALID_ID = -1;
    private TreeSet<Receptor> receptores = new TreeSet<Receptor>();
    private HashMap<Integer, Long> tiempos = new HashMap<Integer, Long>(); // <idReceptor,tiempoUltimoHearbeat>
    private static Integer nextID = 0;
    private Long tiempoUltModif = new Long(0);

    public Directorio() {
        super();
    }


    public static Integer getNextID() {
        return nextID;
    }

    public TreeSet<Receptor> getReceptores() {
        return receptores;
    }

    public HashMap<Integer, Long> getTiempos() {
        return tiempos;
    }

    public void heartbeatRecibido(Receptor receptor) {
        synchronized(receptores){
            
            if (this.receptores.contains(receptor))//TODO REVISAR SI ALGUIEN cAMBIO SU IP O SU PUERTO
                this.receptores.remove(receptor); //por si alguien cambia de IP, puerto o nombre
            else{
                this.updateTiempoUltModif();//cuando llega alguien ya sabe
            }
            this.receptores.add(receptor);
        }

        synchronized (tiempos) {
            this.tiempos.put(receptor.getID(), GregorianCalendar.getInstance().getTimeInMillis());
        }
        // lo metes al treeset y pones el tiempo actual en tiempos

    }

    public Collection<Receptor> listaDestinatariosRegistrados() {
        Long tiempoActual = GregorianCalendar.getInstance().getTimeInMillis();
        synchronized(tiempoUltModif){
        synchronized (receptores) {
            for (Object obj : receptores.toArray()) {
                Receptor receptor = (Receptor) obj;
                boolean online;
                synchronized (tiempos) {
                    online = (tiempoActual - this.tiempos.get(receptor.getID())) <= TIEMPO_TIMEOUT;
                }
                if((receptor.isConectado() && !online) || (!receptor.isConectado() && online)) // SE FUE O VOLVIO
                {
                    this.updateTiempoUltModif();
                }
                
                receptor.setConectado(online);

            }
        }
        }

        return this.getReceptores();
    }

    public static void incrementNextID() {
        Directorio.nextID++;
    }

    public long getTiempoUltModif() {
        return this.tiempoUltModif;
    }

    private void updateTiempoUltModif() {
        this.tiempoUltModif = new GregorianCalendar().getTimeInMillis();
    }
}
