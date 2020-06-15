package emisor.persistencia;

import emisor.modelo.IDatosEmisor;
import emisor.modelo.IMensaje;
import emisor.modelo.IMensaje;
import emisor.modelo.MensajeConComprobante;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import java.util.HashMap;

import java.util.Iterator;

import java.util.TreeMap;

import receptor.modelo.Comprobante;

public class PersistenciaMensajesEmisorXML implements IPersistenciaMensajesEmisor {


    public static final String MENSAJES_COMPROBANTE_FILE_PATH = "Mensajes_Con_Comprobante.xml"; //<idMensaje,IMensaje>
    public static final String MENSAJES_NOENVIADOS_FILE_PATH = "Mensajes_No_Enviados.xml"; //<idMensaje,IMensaje>

    private HashMap<Integer, MensajeConComprobante> mensajesConComprobante; //estos tambien son los no encriptados
    private ArrayList<IMensaje> mensajesNoEnviados; //estos, son los si encriptados , no en viados
    //los si encriptados, si enviados, no se guardan
    //al igual que los mensajes sin comprobante

    private Integer nextIdNoEnviados =
        -2; //solo IDs negativas para diferenciarlas de aquellas asignadas por el servidor.

    @Override
    public int getNextIdNoEnviados() {
        synchronized (nextIdNoEnviados) {
            return nextIdNoEnviados;
        }

    }
    
    @Override
    public void pasarASiguienteIdNoEnviados() {
        synchronized (nextIdNoEnviados) {
            nextIdNoEnviados--;
        }
    }

    public PersistenciaMensajesEmisorXML() {
        super();
        inicializarConComprobante();
        inicializarNoEnviados();
    }

    private void inicializarConComprobante() {
        try {
            XMLDecoder decoder =
                new XMLDecoder(new BufferedInputStream(new FileInputStream(MENSAJES_COMPROBANTE_FILE_PATH)));
            this.mensajesConComprobante = (HashMap<Integer, MensajeConComprobante>) decoder.readObject();
            decoder.close();
            if(this.mensajesConComprobante ==null )
                mensajesConComprobante = new HashMap<Integer, MensajeConComprobante>();
            
        } catch (IOException e) {
            //e.printStackTrace();
            mensajesConComprobante = new HashMap<Integer, MensajeConComprobante>();
        }
    }

    private void inicializarNoEnviados() {
        int ultimaIdUtilizada = -2;

        try {
            XMLDecoder decoder =
                new XMLDecoder(new BufferedInputStream(new FileInputStream(MENSAJES_NOENVIADOS_FILE_PATH)));
            this.mensajesNoEnviados = (ArrayList<IMensaje>) decoder.readObject();
            decoder.close();
            if (this.mensajesNoEnviados == null)
                mensajesNoEnviados = new ArrayList<IMensaje>();

            System.out.println("bien");
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("catch");
            mensajesNoEnviados = new ArrayList<IMensaje>();
        }

        if (mensajesNoEnviados.size() > 0)
            for (IMensaje mensaje: mensajesNoEnviados)
                if(mensaje.getId()<ultimaIdUtilizada)
                    ultimaIdUtilizada = mensaje.getId();
            
        this.nextIdNoEnviados=ultimaIdUtilizada--;

        System.out.println("En la lista hay:" + mensajesNoEnviados);
    }


    @Override
    public void guardarComp(Comprobante comprobante) {
        synchronized (mensajesConComprobante) {
            MensajeConComprobante mensajeC = mensajesConComprobante.get(comprobante.getidMensaje());
            if (mensajeC != null && !mensajeC.getReceptoresConfirmados().contains(comprobante.getUsuarioReceptor()))
                mensajeC.addReceptorConfirmado(comprobante.getUsuarioReceptor());

            persistirConComprobante();
        }

    }

    private void persistirConComprobante() {
        synchronized (MENSAJES_COMPROBANTE_FILE_PATH) {
            XMLEncoder encoder;
            try {
                encoder =
                    new XMLEncoder(new BufferedOutputStream(new FileOutputStream(MENSAJES_COMPROBANTE_FILE_PATH)));
                encoder.writeObject(mensajesConComprobante);
                encoder.close();
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
            }
        }

    }

    @Override
    public Collection<MensajeConComprobante> obtenerMsjsComprobadosEmisor() {


        synchronized (mensajesConComprobante) {
            return mensajesConComprobante.values();
        }

    }

    @Override
    public void marcarMensajesPendientesComoEnviados(Collection<IMensaje> mensajesPendientes) {
        synchronized (mensajesNoEnviados) {

            ArrayList<IMensaje> mensajes2 = new ArrayList<IMensaje>();
            //todo quizas se salva
            for (IMensaje m : mensajesPendientes) {
                mensajes2.add(m.clone());

            }

            for (IMensaje m : mensajes2) {
                mensajesNoEnviados.remove(m);
            }


            persistirNoEnviados();
        }


    }

    private void persistirNoEnviados() {

        synchronized (MENSAJES_NOENVIADOS_FILE_PATH) {
            XMLEncoder encoder;
            try {
                encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(MENSAJES_NOENVIADOS_FILE_PATH)));
                encoder.writeObject(mensajesNoEnviados);
                encoder.close();
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
            }
        }
    }

    @Override
    public void guardarMensajeEncriptado(IMensaje mensaje) {
        synchronized (mensajesNoEnviados) {
            
            
            
            
            
            
            
            this.mensajesNoEnviados.add(mensaje);
            this.persistirNoEnviados();
        }


    }

    @Override
    public void guardarMensajeConComprobante(MensajeConComprobante mensaje) {
        synchronized (this.mensajesConComprobante) {
            this.mensajesConComprobante.put(mensaje.getId(), mensaje);
            this.persistirConComprobante();
        }


    }


    @Override
    public boolean isComprobado(MensajeConComprobante mensajeSeleccionado, String usuarioReceptor) {

        return mensajeSeleccionado.getReceptoresConfirmados().contains(usuarioReceptor);
    }

    @Override
    public Collection<IMensaje> getMensajesNoEnviados() {
        synchronized (mensajesNoEnviados) {
            return this.mensajesNoEnviados;
        }
    }

    @Override
    public boolean quedanMensajesPendientes() {
        synchronized (mensajesNoEnviados) {
            return !this.mensajesNoEnviados.isEmpty();
        }
    }

    @Override
    public void actualizarIdMensaje(int viejaId, int nuevaId) {
        synchronized(mensajesConComprobante){
            
            MensajeConComprobante mensajeACambiar = this.mensajesConComprobante.remove(viejaId);
            if(mensajeACambiar != null){ //si es mensaje con comprobante
                mensajeACambiar.setId(nuevaId);
                this.mensajesConComprobante.put(nuevaId,mensajeACambiar);
                this.persistirConComprobante();
            }
        }

    }
}
