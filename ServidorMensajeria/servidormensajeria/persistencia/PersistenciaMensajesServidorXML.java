package servidormensajeria.persistencia;

import emisor.modelo.IDatosEmisor;
import emisor.modelo.IDatosEmisor;
import emisor.modelo.IMensaje;
import emisor.modelo.IMensaje;
import emisor.modelo.MensajeConAlerta;
import emisor.modelo.MensajeConComprobante;

import java.beans.XMLDecoder;

import java.beans.XMLEncoder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import java.util.HashMap;
import java.util.TreeMap;

import receptor.modelo.IComprobante;
import receptor.modelo.IComprobante;
import receptor.modelo.IDatosReceptor;
import receptor.modelo.IDatosReceptor;

public class PersistenciaMensajesServidorXML implements IPersistenciaMensajesServidor {
    public static final String MENSAJES_COMUNES_FILE_PATH = "Mensajes_Normales.xml"; //<idMensaje,IMensaje>
    public static final String MENSAJES_CONALERTA_FILE_PATH = "Mensajes_ConAlerta.xml"; //<idMensaje,IMensaje>
    public static final String MENSAJES_CONCOMPROBANTE_FILE_PATH = "Mensajes_ConComprobante.xml"; //<idMensaje,IMensaje>
    public static final String COMPROBANTES_SIN_ENVIAR_FILE_PATH = "Comprobantes_Sin_Enviar.xml"; //<nombreEmisor,Collection<IComprobante>>
    public static final String MENSAJES_ENVIADOS_RECEPTORES_FILE_PATH =
        "IdMensajesEnviadosReceptores.xml"; //<usuarioReceptor,Collection<idMensaje>>
    public static final String MENSAJES_PENDIENTES_RECEPTORES_FILE_PATH =
        "IdMensajesPendientesReceptores.xml"; //<usuarioReceptor,Collection<idMensaje>>
    public static final String MENSAJES_CON_COMPROBANTE_EMISORES_FILE_PATH =
        "IdMensajesConComprobanteEmisores.xml"; //<nombreEmisor, <Collection<idMensaje> >

    private TreeMap<Integer, IMensaje> mensajes; //usamos TreeMap porque es serializable

    private TreeMap<Integer, IMensaje> mensajesComunes = new TreeMap<Integer, IMensaje>(); //SDMOP
    private TreeMap<Integer, MensajeConAlerta> mensajesConAlerta = new TreeMap<Integer, MensajeConAlerta>(); //SDMOP
    private TreeMap<Integer, MensajeConComprobante> mensajesConComprobante =
        new TreeMap<Integer, MensajeConComprobante>(); //SDMOP

    private TreeMap<String, Collection<Integer>> idMensajesEntregadosRecep;
    private TreeMap<String, Collection<Integer>> idMensajesEntregarRecep;
    private TreeMap<String, Collection<Integer>> idMensajesConComprobEmisores;

    private Integer proximoIdMensaje;
    private HashMap<String,Collection<IComprobante>> comprobantesNoEnviados;//<nombreEmisor,Collection<IComprobante>>

    public PersistenciaMensajesServidorXML() {
        super();
        cargaInicialMensajes();
        cargaInicialIdMensajesEntregadosRecep();
        cargaInicialIdMensajesEntregarRecep();
        cargaInicialidMensajesConComprobEmisores();
        cargaInicialComprobantesNoEnviados();
        proximoIdMensaje = cargarMaxIdMsjGuardado() + 1;
    }


    /**
     * Pre: llamar a este metodo solamente en instanciacion de PersistenciaMensajesServidorJSON,
     *      los mensajes ya estan cargados
     * @return
     */
    private int cargarMaxIdMsjGuardado() {
        if (mensajes.size() == 0)
            return -1;
        else
            return Collections.max(mensajes.keySet());
    }

    private void cargaInicialMensajes() {
            //separados por que lagun archivo puede no existir... y esta bien
            try {
                XMLDecoder decoderNormales =
                    new XMLDecoder(new BufferedInputStream(new FileInputStream(MENSAJES_COMUNES_FILE_PATH)));
                this.mensajesComunes = (TreeMap<Integer, IMensaje>) decoderNormales.readObject();
                decoderNormales.close();
            } catch (IOException e) {
                mensajesComunes = new TreeMap<Integer, IMensaje>();
            }
            
            try{
            XMLDecoder decoderConAlerta =
                new XMLDecoder(new BufferedInputStream(new FileInputStream(MENSAJES_CONALERTA_FILE_PATH)));
                this.mensajesConAlerta = (TreeMap<Integer, MensajeConAlerta>) decoderConAlerta.readObject();
                decoderConAlerta.close();
            }
            catch(IOException e){
                mensajesConAlerta = new TreeMap<Integer,MensajeConAlerta>();
            }
            
            try{
            XMLDecoder decoderConComprobante =
                new XMLDecoder(new BufferedInputStream(new FileInputStream(MENSAJES_CONCOMPROBANTE_FILE_PATH)));
                this.mensajesConComprobante = (TreeMap<Integer, MensajeConComprobante>) decoderConComprobante.readObject();
                decoderConComprobante.close();
            }
            catch(IOException e){
                this.mensajesConComprobante = new TreeMap<Integer,MensajeConComprobante>();
            }

            mensajes = new TreeMap<Integer, IMensaje>();
            //a ensamblar
            for (IMensaje mensaje : mensajesComunes.values())
                mensajes.put(mensaje.getId(), mensaje);

            for (IMensaje mensaje : mensajesConAlerta.values())
                mensajes.put(mensaje.getId(), mensaje);

            for (IMensaje mensaje : mensajesConComprobante.values())
                mensajes.put(mensaje.getId(), mensaje);

            if (mensajes == null) //se fija si es null porque puede pasar si el archivo existe pero esta vacio
                mensajes = new TreeMap<Integer, IMensaje>();

    }

    private void cargaInicialIdMensajesEntregadosRecep() {
        try {
            XMLDecoder decoder =
                new XMLDecoder(new BufferedInputStream(new FileInputStream(MENSAJES_ENVIADOS_RECEPTORES_FILE_PATH)));
            idMensajesEntregadosRecep = (TreeMap<String, Collection<Integer>>) decoder.readObject();
            decoder.close();
            if (idMensajesEntregadosRecep ==
                null) //se fija si es null porque puede pasar si el archivo existe pero esta vacio
                idMensajesEntregadosRecep = new TreeMap<String, Collection<Integer>>();
        } catch (IOException e) {
            idMensajesEntregadosRecep = new TreeMap<String, Collection<Integer>>();
        }
    }

    private void cargaInicialIdMensajesEntregarRecep() {
        try {
            XMLDecoder decoder =
                new XMLDecoder(new BufferedInputStream(new FileInputStream(MENSAJES_PENDIENTES_RECEPTORES_FILE_PATH)));
            idMensajesEntregarRecep = (TreeMap<String, Collection<Integer>>) decoder.readObject();
            decoder.close();
            if (idMensajesEntregarRecep ==
                null) //se fija si es null porque puede pasar si el archivo existe pero esta vacio
                idMensajesEntregarRecep = new TreeMap<String, Collection<Integer>>();
        } catch (IOException e) {
            idMensajesEntregarRecep = new TreeMap<String, Collection<Integer>>();
        }
    }

    private void cargaInicialidMensajesConComprobEmisores() {
        try {
            XMLDecoder decoder =
                new XMLDecoder(new BufferedInputStream(new FileInputStream(MENSAJES_CON_COMPROBANTE_EMISORES_FILE_PATH)));
            idMensajesConComprobEmisores = (TreeMap<String, Collection<Integer>>) decoder.readObject();
            decoder.close();
            if (idMensajesConComprobEmisores ==
                null) //se fija si es null porque puede pasar si el archivo existe pero esta vacio
                idMensajesConComprobEmisores = new TreeMap<String, Collection<Integer>>();
        } catch (IOException e) {
            idMensajesConComprobEmisores = new TreeMap<String, Collection<Integer>>();
        }
    }
    
    private void cargaInicialComprobantesNoEnviados(){
        try {
            XMLDecoder decoder =
                new XMLDecoder(new BufferedInputStream(new FileInputStream(COMPROBANTES_SIN_ENVIAR_FILE_PATH)));
            this.comprobantesNoEnviados = (HashMap<String, Collection<IComprobante>>) decoder.readObject();
            decoder.close();
            if (comprobantesNoEnviados ==
                null) //se fija si es null porque puede pasar si el archivo existe pero esta vacio
                comprobantesNoEnviados = new HashMap<String, Collection<IComprobante>>();
        } catch (IOException e) {
            comprobantesNoEnviados = new HashMap<String, Collection<IComprobante>>();
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
    public void guardarMsj(IMensaje mensaje, String usuarioReceptor, boolean entregado) throws Exception {
        //System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ");
        try {
            synchronized (mensajes) {
                if (!mensajes.containsKey(mensaje.getId())) { //solo se guarda el mensaje 1 vez
                    mensajes.put(mensaje.getId(), mensaje);

                    if (mensaje instanceof MensajeConAlerta) { //SDMOP
                        this.mensajesConAlerta.put(mensaje.getId(), (MensajeConAlerta) mensaje);
                        synchronized (MENSAJES_CONALERTA_FILE_PATH) {
                            XMLEncoder encoder =
                                new XMLEncoder(new BufferedOutputStream(new FileOutputStream(MENSAJES_CONALERTA_FILE_PATH)));
                            encoder.writeObject(mensajesConAlerta);
                            encoder.close();
                        }
                    } else if (mensaje instanceof MensajeConComprobante) { //SDMOP
                        this.mensajesConComprobante.put(mensaje.getId(), (MensajeConComprobante) mensaje);

                        synchronized (MENSAJES_CONCOMPROBANTE_FILE_PATH) {

                            XMLEncoder encoder =
                                new XMLEncoder(new BufferedOutputStream(new FileOutputStream(MENSAJES_CONCOMPROBANTE_FILE_PATH)));
                            encoder.writeObject(mensajesConComprobante);
                            encoder.close();
                        }
                    } else if (mensaje instanceof IMensaje) { //SDMOP
                        this.mensajesComunes.put(mensaje.getId(), mensaje);

                        synchronized (MENSAJES_COMUNES_FILE_PATH) {
                            XMLEncoder encoder =
                                new XMLEncoder(new BufferedOutputStream(new FileOutputStream(MENSAJES_COMUNES_FILE_PATH)));
//                            System.out.println("A");
                            
                            encoder.writeObject(mensajesComunes);
                            
//                            System.out.println("B");
                            encoder.close();
                        }
                    }


                }
            }
        } catch (Exception e) {
            System.out.println("Error de persistencia XML");
            //e.printStackTrace();
        }


        if (entregado) {
            marcarMensajeEnviado(mensaje, usuarioReceptor, true);
        } else {
            synchronized (idMensajesEntregarRecep) {
                Collection<Integer> idMensajesNoRecibidos;
                if (idMensajesEntregarRecep.containsKey(usuarioReceptor)) {
                    idMensajesNoRecibidos = idMensajesEntregarRecep.remove(usuarioReceptor);
                } else {
                    idMensajesNoRecibidos = new ArrayList<Integer>();
                }
                idMensajesNoRecibidos.add(mensaje.getId());
                idMensajesEntregarRecep.put(usuarioReceptor, idMensajesNoRecibidos);


                synchronized (MENSAJES_PENDIENTES_RECEPTORES_FILE_PATH) {
                    XMLEncoder encoder =
                        new XMLEncoder(new BufferedOutputStream(new FileOutputStream(MENSAJES_PENDIENTES_RECEPTORES_FILE_PATH)));
                    encoder.writeObject(idMensajesEntregarRecep);
                    encoder.close();
                }
            }


        }

        if (mensaje instanceof MensajeConComprobante) {
            String nombreEmisor = mensaje.getEmisor().getNombre();
            synchronized (idMensajesConComprobEmisores) {
                Collection<Integer> idMensajesComprobadoAct;
                if (idMensajesConComprobEmisores.containsKey(nombreEmisor)) {
                    idMensajesComprobadoAct = idMensajesConComprobEmisores.remove(nombreEmisor);
                } else {
                    idMensajesComprobadoAct = new ArrayList<Integer>();
                }
                idMensajesComprobadoAct.add(mensaje.getId());
                idMensajesConComprobEmisores.put(nombreEmisor, idMensajesComprobadoAct);

                synchronized (MENSAJES_CON_COMPROBANTE_EMISORES_FILE_PATH) {
                    XMLEncoder encoder =
                        new XMLEncoder(new BufferedOutputStream(new FileOutputStream(MENSAJES_CON_COMPROBANTE_EMISORES_FILE_PATH)));
                    encoder.writeObject(idMensajesConComprobEmisores);
                    encoder.close();
                }

            }


        }

    }

    @Override
    public void guardarComp(IComprobante comprobante) throws Exception {

        MensajeConComprobante mensaje;
        synchronized (mensajes) {
            mensaje = (MensajeConComprobante) mensajes.get(comprobante.getidMensaje());
            mensaje.addReceptorConfirmado(comprobante.getUsuarioReceptor());
            mensajes.put(mensaje.getId(),
                         mensaje); //son el mismo objeto referenciado desde 2 listas distintas asi que no pasa nada

            synchronized (MENSAJES_CONCOMPROBANTE_FILE_PATH) {
                XMLEncoder encoder =
                    new XMLEncoder(new BufferedOutputStream(new FileOutputStream(MENSAJES_CONCOMPROBANTE_FILE_PATH)));
                encoder.writeObject(mensajesConComprobante);
                encoder.close();
            }
        }


    }

    @Override
    public Collection<IMensaje> obtenerMsjsPendientesReceptor(IDatosReceptor receptor) throws Exception {
        Collection<IMensaje> mensajesParaReceptor = new ArrayList<IMensaje>();

        synchronized (idMensajesEntregarRecep) {
            Collection<Integer> idMensajesEntregarRecepAct = idMensajesEntregarRecep.get(receptor.getUsuario());
            if (idMensajesEntregarRecepAct != null) { //si tiene mensajes pendientes
                synchronized (mensajes) {
                    for (int id : idMensajesEntregarRecepAct) {
                        mensajesParaReceptor.add(mensajes.get(id));
                    }
                }
            }
        }

        return mensajesParaReceptor;
    }

    @Override
    public Collection<MensajeConComprobante> obtenerMsjsComprobadosEmisor(IDatosEmisor emisor) throws Exception {
        Collection<MensajeConComprobante> mensajesComprobados = new ArrayList<MensajeConComprobante>();
        String nombreEmisor = emisor.getNombre();
        synchronized (idMensajesConComprobEmisores) {
            Collection<Integer> idMensajesComprobadosAct = idMensajesConComprobEmisores.get(nombreEmisor);
            if (idMensajesComprobadosAct != null) { //si tiene mensajes con comprobante
                synchronized (mensajes) {
                    for (int id : idMensajesComprobadosAct) {
                        mensajesComprobados.add((MensajeConComprobante) mensajes.get(id));
                    }
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
    public void marcarMensajeEnviado(IMensaje mensaje, String usuarioReceptor, boolean primerIntento) throws Exception {

        if (!primerIntento) { //si estaba marcado para entregar hay que sacar
            synchronized (idMensajesEntregarRecep) {
                Collection<Integer> idMensajesRecibidos;
                idMensajesRecibidos =
                    idMensajesEntregarRecep.remove(usuarioReceptor); //usuario receptor existia porque no es el primer intento
                idMensajesRecibidos.remove(mensaje.getId());
                idMensajesEntregarRecep.put(usuarioReceptor, idMensajesRecibidos);

                synchronized (MENSAJES_PENDIENTES_RECEPTORES_FILE_PATH) {

                    XMLEncoder encoder =
                        new XMLEncoder(new BufferedOutputStream(new FileOutputStream(MENSAJES_PENDIENTES_RECEPTORES_FILE_PATH)));
                    encoder.writeObject(idMensajesEntregarRecep);
                    encoder.close();
                }
            }


        }

        synchronized (idMensajesEntregadosRecep) {
            Collection<Integer> idMensajesRecibidos;
            if (idMensajesEntregadosRecep.containsKey(usuarioReceptor)) {
                idMensajesRecibidos = idMensajesEntregadosRecep.remove(usuarioReceptor);
            } else {
                idMensajesRecibidos = new ArrayList<Integer>();
            }
            idMensajesRecibidos.add(mensaje.getId());
            idMensajesEntregadosRecep.put(usuarioReceptor, idMensajesRecibidos);


            synchronized (MENSAJES_ENVIADOS_RECEPTORES_FILE_PATH) {
                
                XMLEncoder encoder =
                    new XMLEncoder(new BufferedOutputStream(new FileOutputStream(MENSAJES_ENVIADOS_RECEPTORES_FILE_PATH)));
                encoder.writeObject(idMensajesEntregadosRecep);
                encoder.close();
            }
        }


    }

    @Override
    public void avanzaProximoIdMensaje() {
        synchronized (this.proximoIdMensaje) {
            this.proximoIdMensaje++;
        }
    }

    @Override
    public int getProximoIdMensaje() {
        synchronized (this.proximoIdMensaje) {
            return proximoIdMensaje;
        }
    }

    @Override
    public void guardarComprobanteNoEnviado(IComprobante comprobante) throws FileNotFoundException {
        synchronized(comprobantesNoEnviados){
            Collection<IComprobante> coleccion = this.comprobantesNoEnviados.get(comprobante.getEmisorOriginal().getNombre());
            if(coleccion==null){
                coleccion = new ArrayList<IComprobante>();
                this.comprobantesNoEnviados.put(comprobante.getEmisorOriginal().getNombre(),coleccion);
            }
                    
            
            coleccion.add(comprobante);
            
            
            synchronized (COMPROBANTES_SIN_ENVIAR_FILE_PATH) {
                XMLEncoder encoder =
                    new XMLEncoder(new BufferedOutputStream(new FileOutputStream(COMPROBANTES_SIN_ENVIAR_FILE_PATH)));
                encoder.writeObject(comprobantesNoEnviados);
                encoder.close();
            }
        }
    }
    
    
    @Override
    public Collection<IComprobante> getComprobantesNoEnviados(IDatosEmisor emisor){
        synchronized(comprobantesNoEnviados){
            return this.comprobantesNoEnviados.get(emisor.getNombre());
        }
    }
    
    @Override
    public void eliminarComprobantesNoEnviados(IDatosEmisor emisor) throws FileNotFoundException{
        synchronized(comprobantesNoEnviados){
            this.comprobantesNoEnviados.get(emisor.getNombre()).clear();
            
            synchronized (COMPROBANTES_SIN_ENVIAR_FILE_PATH) {
                XMLEncoder encoder =
                    new XMLEncoder(new BufferedOutputStream(new FileOutputStream(COMPROBANTES_SIN_ENVIAR_FILE_PATH)));
                encoder.writeObject(comprobantesNoEnviados);
                encoder.close();
            }
        }
        
        
    }
}
