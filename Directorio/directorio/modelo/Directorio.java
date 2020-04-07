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
    TreeSet<Receptor> receptores = new TreeSet<Receptor>();
    HashMap<Integer, Long> tiempos = new HashMap<Integer, Long>(); // <idReceptor,tiempoUltimoHearbeat>

    public Directorio() {
        super();
    }

    public TreeSet<Receptor> getReceptores() {
        return receptores;
    }

    public HashMap<Integer, Long> getTiempos() {
        return tiempos;
    }

    public void heartbeatRecibido(Receptor receptor) {
        
        if (this.receptores.contains(receptor)) {
            this.receptores.remove(receptor); //por si alguien cambia de IP, puerto o nombre
        }
        this.receptores.add(receptor);


        this.tiempos.put(receptor.getID(), GregorianCalendar.getInstance().getTimeInMillis());
        // lo metes al treeset y pones el tiempo actual en tiempos

    }

    public Collection<Receptor> getDestinatariosRegistrados() {
        Long tiempoActual = GregorianCalendar.getInstance().getTimeInMillis();
        TreeSet<Receptor> destinatariosRegistrados = new TreeSet<Receptor>();
        
        for(Object obj : receptores.toArray()){
            Receptor receptor = (Receptor) obj;
            boolean online = (tiempoActual-this.tiempos.get(receptor.getID()))<=TIEMPO_TIMEOUT;
            receptor.setConectado(online);
            destinatariosRegistrados.add(receptor);
            
        }
        
        return destinatariosRegistrados;
    }
}
