package directorio.modelo;

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

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

public class Directorio {
    private static int TIEMPO_TIMEOUT = 500;
    private TreeMap<String, Receptor> receptores = new TreeMap<String, Receptor>();
    private HashMap<String, Long> tiempos = new HashMap<String, Long>(); // <usuarioReceptor,tiempoUltimoHearbeat>
    private Long tiempoUltModif = new Long(0);

    public Directorio() {
        super();
    }


    /**
     *
     * @return Coleccion ordenada por usuario de receptores
     */
    public Collection<Receptor> getReceptores() {
        synchronized (receptores) {
            return new ArrayList(receptores.values());
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

            if (this.receptores.containsKey(receptor.getUsuario())) { 
                Receptor receptorAnt = receptores.remove(receptor.getUsuario());
                if ( !receptorAnt.isConectado() || !Receptor.equalsDatosNoIdentif(receptorAnt, receptor)){
                    //detecta si alguien cambia de estado de conexion (de desconectado a conectado), IP, puerto o nombre 
                    receptor.setConectado(true); //acaba de llegar por lo que esta conectado
                    this.updateTiempoUltModif();
                }
            } else {
                receptor.setConectado(true); //acaba de llegar por lo que esta conectado
                this.updateTiempoUltModif(); //cuando llega alguien ya sabe
            }
            this.receptores.put(receptor.getUsuario(), receptor);
        }
        
        synchronized (tiempos) {
            this.tiempos.put(receptor.getUsuario(), GregorianCalendar.getInstance().getTimeInMillis());
        }
        // lo metes al treemap y pones el tiempo actual en tiempos

    }

    public Collection<Receptor> listaDestinatariosRegistrados() {
        Long tiempoActual = GregorianCalendar.getInstance().getTimeInMillis();
        boolean alguienSeFue = false;
        
        synchronized (receptores) {
            for (Object obj : receptores.values()) {
                Receptor receptor = (Receptor) obj;
                boolean online;
                synchronized (tiempos) {
                    online = (tiempoActual - this.tiempos.get(receptor.getUsuario())) <= TIEMPO_TIMEOUT;
                }
                if (!alguienSeFue && (receptor.isConectado() && !online)) // se fue alguien
                {
                    alguienSeFue = true;
                }

                receptor.setConectado(online);

            }
        }
       
        
        if(alguienSeFue)
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
}
