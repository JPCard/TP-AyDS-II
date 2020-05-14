package servidormensajeria.persistencia;

import emisor.modelo.Emisor;
import emisor.modelo.Mensaje;
import emisor.modelo.MensajeConComprobante;

import java.beans.XMLDecoder;

import java.beans.XMLEncoder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import java.util.TreeMap;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

public class PersistenciaMensajesServidorXML implements IPersistenciaMensajesServidor {
    public static final String MENSAJES_FILE_PATH = "Mensajes.xml"; //<idMensaje,Mensaje>
    public static final String MENSAJES_ENVIADOS_RECEPTORES_FILE_PATH = "IdMensajesEnviadosReceptores.xml";      //<usuarioReceptor,Collection<idMensaje>>
    public static final String MENSAJES_PENDIENTES_RECEPTORES_FILE_PATH = "IdMensajesPendientesReceptores.xml";  //<usuarioReceptor,Collection<idMensaje>>
    public static final String MENSAJES_CON_COMPROBANTE_EMISORES_FILE_PATH = "IdMensajesConComprobanteEmisores.xml";    //<nombreEmisor, <Collection<idMensaje> >

    private TreeMap<Integer,Mensaje> mensajes; //usamos TreeMap porque es serializable
    private TreeMap<String,Collection<Integer>> idMensajesEntregadosRecep;
    private TreeMap<String,Collection<Integer>> idMensajesEntregarRecep;
    private TreeMap<String,Collection<Integer>> idMensajesConComprobEmisores;
    
    private Integer proximoIdMensaje;
    
    public PersistenciaMensajesServidorXML() {
        super();
        cargaInicialMensajes(); 
        cargaInicialIdMensajesEntregadosRecep();
        cargaInicialIdMensajesEntregarRecep();
        cargaInicialidMensajesConComprobEmisores();
        proximoIdMensaje = cargarMaxIdMsjGuardado() + 1;
    }
    
    
    /**
     * Pre: llamar a este metodo solamente en instanciacion de PersistenciaMensajesServidorJSON,
     *      los mensajes ya estan cargados
     * @return
     */
    private int cargarMaxIdMsjGuardado(){
        if(mensajes.size() == 0) 
            return -1;
        else 
            return Collections.max(mensajes.keySet());
    }
    
    private void cargaInicialMensajes(){
        try {
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream( new FileInputStream(MENSAJES_FILE_PATH)));
            mensajes = (TreeMap<Integer, Mensaje>) decoder.readObject();
            decoder.close();
            if(mensajes == null)//se fija si es null porque puede pasar si el archivo existe pero esta vacio
                mensajes = new TreeMap<Integer,Mensaje>(); 
        } catch (IOException e) {
            mensajes = new TreeMap<Integer,Mensaje>();
        }
    }
    
    private void cargaInicialIdMensajesEntregadosRecep(){
        try {
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream( new FileInputStream(MENSAJES_ENVIADOS_RECEPTORES_FILE_PATH)));
            idMensajesEntregadosRecep = (TreeMap<String, Collection<Integer>>) decoder.readObject();
            decoder.close();
            if(idMensajesEntregadosRecep == null)//se fija si es null porque puede pasar si el archivo existe pero esta vacio
                idMensajesEntregadosRecep = new TreeMap<String,Collection<Integer>>();
        } catch (IOException e) {
            idMensajesEntregadosRecep = new TreeMap<String,Collection<Integer>>();
        }
    }
    
    private void cargaInicialIdMensajesEntregarRecep(){
        try {
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream( new FileInputStream(MENSAJES_PENDIENTES_RECEPTORES_FILE_PATH)));
            idMensajesEntregarRecep = (TreeMap<String, Collection<Integer>>) decoder.readObject();
            decoder.close();
            if(idMensajesEntregarRecep == null)//se fija si es null porque puede pasar si el archivo existe pero esta vacio
                idMensajesEntregarRecep = new TreeMap<String,Collection<Integer>>();
        } catch (IOException e) {
            idMensajesEntregarRecep = new TreeMap<String,Collection<Integer>>();
        }
    }
    
    private void cargaInicialidMensajesConComprobEmisores(){
        try {
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream( new FileInputStream(MENSAJES_CON_COMPROBANTE_EMISORES_FILE_PATH)));
            idMensajesConComprobEmisores = (TreeMap<String, Collection<Integer>>) decoder.readObject();
            decoder.close();
            if(idMensajesConComprobEmisores == null)//se fija si es null porque puede pasar si el archivo existe pero esta vacio
                idMensajesConComprobEmisores = new TreeMap<String,Collection<Integer>>();
        } catch (IOException e) {
            idMensajesConComprobEmisores = new TreeMap<String,Collection<Integer>>();
        }
    }
    

    /**
     * Pre: mensaje no estaba en mensajes,
     *      haber intentado mandar el mensaje para saber el estado de entregado.
     * 
     * Este metodo se llama luego de intentar mandarle el mensaje a cada receptor
     * @param mensaje - mensaje a guardar
     * @throws Exception
     */
    @Override
    public void guardarMsj(Mensaje mensaje, String usuarioReceptor, boolean entregado) throws Exception {
        
        
        synchronized (mensajes){
            if(!mensajes.containsKey(mensaje.getId())){ //solo se guarda el mensaje 1 vez
                mensajes.put(mensaje.getId(), mensaje);
                
                synchronized (MENSAJES_FILE_PATH){
                    XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(MENSAJES_FILE_PATH)));
                    encoder.writeObject(mensajes);
                    encoder.close();
                }
            }
        }
        
        
        
        if(entregado){
            marcarMensajeEnviado(mensaje, usuarioReceptor, true);
        }
        else{
            synchronized (idMensajesEntregarRecep){
                Collection<Integer> idMensajesNoRecibidos;
                if(idMensajesEntregarRecep.containsKey(usuarioReceptor)){
                    idMensajesNoRecibidos = idMensajesEntregarRecep.remove(usuarioReceptor);
                }
                else{
                    idMensajesNoRecibidos = new ArrayList<Integer>();
                }
                idMensajesNoRecibidos.add(mensaje.getId());
                idMensajesEntregarRecep.put(usuarioReceptor, idMensajesNoRecibidos);
                
                
                synchronized (MENSAJES_PENDIENTES_RECEPTORES_FILE_PATH){
                    XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(MENSAJES_PENDIENTES_RECEPTORES_FILE_PATH)));
                    encoder.writeObject(idMensajesEntregarRecep);
                    encoder.close();
                }
            }
            
           
        }
        
        if(mensaje instanceof MensajeConComprobante){
            String nombreEmisor = mensaje.getEmisor().getNombre();
            synchronized (idMensajesConComprobEmisores){
                Collection<Integer> idMensajesComprobadoAct;
                if(idMensajesConComprobEmisores.containsKey(nombreEmisor)){
                    idMensajesComprobadoAct = idMensajesConComprobEmisores.remove(nombreEmisor);
                }
                else{
                    idMensajesComprobadoAct = new ArrayList<Integer>();
                }
                idMensajesComprobadoAct.add(mensaje.getId());
                idMensajesConComprobEmisores.put(nombreEmisor, idMensajesComprobadoAct);
                
                synchronized (MENSAJES_CON_COMPROBANTE_EMISORES_FILE_PATH){
                    XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(MENSAJES_CON_COMPROBANTE_EMISORES_FILE_PATH)));
                    encoder.writeObject(idMensajesConComprobEmisores);
                    encoder.close();
                }
                
            }
            
            
            
            
        }
        
    }

    @Override
    public void guardarComp(Comprobante comprobante) throws Exception {
        String json = "";
        FileWriter file;
        
        MensajeConComprobante mensaje;
        synchronized(mensajes){
            mensaje = (MensajeConComprobante) mensajes.get(comprobante.getidMensaje());
            mensaje.addReceptorConfirmado(comprobante.getUsuarioReceptor());
            mensajes.put(mensaje.getId(),mensaje);
            
            synchronized (MENSAJES_FILE_PATH){
                XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(MENSAJES_FILE_PATH)));
                encoder.writeObject(mensajes);
                encoder.close();
            }
        }
        
        
               
    }

    @Override
    public Collection<Mensaje> obtenerMsjsPendientesReceptor(Receptor receptor) throws Exception {
        Collection<Mensaje> mensajesParaReceptor = new ArrayList<Mensaje>();
        
        synchronized(idMensajesEntregarRecep){
            Collection<Integer> idMensajesEntregarRecepAct = idMensajesEntregarRecep.get(receptor.getUsuario());
            synchronized(mensajes){
                for(int id : idMensajesEntregarRecepAct){
                    mensajesParaReceptor.add(mensajes.get(id));
                }
            }
        }
        
        return mensajesParaReceptor;
    }

    @Override
    public Collection<MensajeConComprobante> obtenerMsjsComprobadosEmisor(Emisor emisor) throws Exception {
        Collection<MensajeConComprobante> mensajesComprobados = new ArrayList<MensajeConComprobante>();
        String nombreEmisor = emisor.getNombre();
        synchronized (idMensajesConComprobEmisores){
            Collection<Integer> idMensajesComprobadosAct = idMensajesConComprobEmisores.get(nombreEmisor);
            synchronized(mensajes){
                for(int id : idMensajesComprobadosAct){
                    mensajesComprobados.add( (MensajeConComprobante) mensajes.get(id));
                }
            }
        }
        
        return mensajesComprobados;
    }

    /**
     * @param mensaje
     * @param receptor
     * @param primerIntento, si es primer intento significa que nunca se guardo como no enviado
     * @throws Exception
     */
    @Override
    public void marcarMensajeEnviado(Mensaje mensaje, String usuarioReceptor, boolean primerIntento) throws Exception {
        String json;
        FileWriter file;
        
        if(!primerIntento){ //si estaba marcado para entregar hay que sacar
            synchronized (idMensajesEntregarRecep){
                Collection<Integer> idMensajesRecibidos;
                idMensajesRecibidos = idMensajesEntregarRecep.remove(usuarioReceptor); //usuario receptor existia porque no es el primer intento
                idMensajesRecibidos.remove(mensaje.getId());
                idMensajesEntregarRecep.put(usuarioReceptor, idMensajesRecibidos);
                
            }
            
            synchronized (MENSAJES_PENDIENTES_RECEPTORES_FILE_PATH){
                
                XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(MENSAJES_PENDIENTES_RECEPTORES_FILE_PATH)));
                encoder.writeObject(idMensajesEntregarRecep);
                encoder.close();
            }
        }
        
        synchronized (idMensajesEntregadosRecep){
            Collection<Integer> idMensajesRecibidos;
            if(idMensajesEntregadosRecep.containsKey(usuarioReceptor)){
                idMensajesRecibidos = idMensajesEntregadosRecep.remove(usuarioReceptor);
            }
            else{
                idMensajesRecibidos = new ArrayList<Integer>();
            }
            idMensajesRecibidos.add(mensaje.getId());
            idMensajesEntregadosRecep.put(usuarioReceptor, idMensajesRecibidos);
            
            
            synchronized (MENSAJES_ENVIADOS_RECEPTORES_FILE_PATH){
                
                XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(MENSAJES_ENVIADOS_RECEPTORES_FILE_PATH)));
                encoder.writeObject(idMensajesEntregadosRecep);
                encoder.close();
            }
        }
        
        
    }

    @Override
    public void avanzaProximoIdMensaje(){
        synchronized(this.proximoIdMensaje){
            this.proximoIdMensaje++;
        }
    }

    @Override
    public int getProximoIdMensaje() {
        synchronized(this.proximoIdMensaje){
            return proximoIdMensaje;
        }
    }
}
