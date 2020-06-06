package emisor.persistencia;

import emisor.modelo.Emisor;
import emisor.modelo.Mensaje;
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
    
    
    public static final String MENSAJES_COMPROBANTE_FILE_PATH = "Mensajes_Con_Comprobante.xml"; //<idMensaje,Mensaje>
    public static final String MENSAJES_NOENVIADOS_FILE_PATH = "Mensajes_No_Enviados.xml"; //<idMensaje,Mensaje>
    
    private HashMap<Integer, MensajeConComprobante> mensajesConComprobante; //estos tambien son los no encriptados
    private ArrayList<Mensaje> mensajesNoEnviados; //estos, son los si encriptados , no en viados
    //los si encriptados, si enviados, no se guardan
    //al igual que los mensajes sin comprobante
    
    public PersistenciaMensajesEmisorXML() {
        super();
        inicializarConComprobante();
        inicializarNoEnviados();
    }
    
    private void inicializarConComprobante(){
        try {
            XMLDecoder decoder =
                new XMLDecoder(new BufferedInputStream(new FileInputStream(MENSAJES_COMPROBANTE_FILE_PATH)));
            this.mensajesConComprobante = (HashMap<Integer, MensajeConComprobante>) decoder.readObject();
            decoder.close();
        } catch (IOException e) {
            //e.printStackTrace();
            mensajesConComprobante = new HashMap<Integer, MensajeConComprobante>();
        }
    }
    
    private void inicializarNoEnviados(){
        try {
            XMLDecoder decoder =
                new XMLDecoder(new BufferedInputStream(new FileInputStream(MENSAJES_NOENVIADOS_FILE_PATH)));
            this.mensajesNoEnviados = (ArrayList<Mensaje>) decoder.readObject();
            decoder.close();
        } catch (IOException e) {
            //e.printStackTrace();
            mensajesNoEnviados  = new ArrayList<Mensaje>();
        }
    }


    @Override
    public void guardarComp(Comprobante comprobante) {
        MensajeConComprobante mensajeC = mensajesConComprobante.get(comprobante.getidMensaje());
        if (mensajeC!=null && !mensajeC.getReceptoresConfirmados().contains(comprobante.getUsuarioReceptor()))
            mensajeC.addReceptorConfirmado(comprobante.getUsuarioReceptor());
        
        persistirConComprobante();
    }
    
    private void persistirConComprobante(){
        synchronized(mensajesConComprobante){
            synchronized(MENSAJES_COMPROBANTE_FILE_PATH){
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
        
    }

    @Override
    public Collection<MensajeConComprobante> obtenerMsjsComprobadosEmisor()  {
        
        
        
        return mensajesConComprobante.values();
    }

    @Override
    public void marcarMensajesEnviados(){
        this.mensajesNoEnviados.clear();
        
        persistirNoEnviados();
    }
    
    private void persistirNoEnviados(){
        synchronized(this.mensajesNoEnviados){
            synchronized(MENSAJES_NOENVIADOS_FILE_PATH){
                XMLEncoder encoder;
                try {
                    encoder =
                        new XMLEncoder(new BufferedOutputStream(new FileOutputStream(MENSAJES_NOENVIADOS_FILE_PATH)));
                    encoder.writeObject(mensajesNoEnviados);
                    encoder.close();
                } catch (FileNotFoundException e) {
                    //e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void guardarMensajeEncriptado(Mensaje mensaje) {
        this.mensajesNoEnviados.add(mensaje);
        this.persistirNoEnviados();
    }

    @Override
    public void guardarMensajeConComprobante(MensajeConComprobante mensaje) {
        
        this.mensajesConComprobante.put(mensaje.getId(),mensaje);
        this.persistirConComprobante();
    }


    @Override
    public Iterator<MensajeConComprobante> getMensajesConComprobanteIterator() {
        
        return this.mensajesConComprobante.values().iterator();
    }

    @Override
    public boolean isComprobado(MensajeConComprobante mensajeSeleccionado, String usuarioReceptor) {
        
        return mensajeSeleccionado.getReceptoresConfirmados().contains(usuarioReceptor);
    }
}
