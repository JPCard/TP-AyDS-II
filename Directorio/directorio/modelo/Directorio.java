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
        synchronized (nextID) {
            synchronized (receptores) {
                if (!(receptor.getID() == INVALID_ID)) {
                    if (this.receptores.contains(receptor)) {
                        this.receptores.remove(receptor); //por si alguien cambia de IP, puerto o nombre
                        this.receptores.add(receptor);
                    }
                    //else aca seria que viene con ID valido pero esta registrado!!!! -> no lo dejamos comunicarse con directorio
                } else { //receptor nuevo por venir con id invalida o era valida pero no estaba en receptores
                    receptor.setID(nextID++);
                    this.receptores.add(receptor);
                }


            }
        }

        synchronized (tiempos) {
            this.tiempos.put(receptor.getID(), GregorianCalendar.getInstance().getTimeInMillis());
        }
        // lo metes al treeset y pones el tiempo actual en tiempos

    }

    public Collection<Receptor> listaDestinatariosRegistrados() {
        Long tiempoActual = GregorianCalendar.getInstance().getTimeInMillis();
        synchronized (receptores) {
            for (Object obj : receptores.toArray()) {
                Receptor receptor = (Receptor) obj;
                boolean online;
                synchronized (tiempos) {
                    online = (tiempoActual - this.tiempos.get(receptor.getID())) <= TIEMPO_TIMEOUT;
                }
                receptor.setConectado(online);

            }
        }


        return this.getReceptores();
    }
}
