
package servidormensajeria.persistencia;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import emisor.modelo.Emisor;
import emisor.modelo.Mensaje;
import emisor.modelo.MensajeConAlerta;
import emisor.modelo.MensajeConComprobante;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import java.lang.reflect.Type;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import java.util.Map;

import java.util.TreeMap;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

public class PersistenciaMensajesServidorJSON implements IPersistenciaMensajesServidor {
   
    public static final String MENSAJES_FILE_PATH = "Mensajes.json"; //<idMensaje,Mensaje>
    public static final String MENSAJES_ENVIADOS_RECEPTORES_FILE_PATH =
        "IdMensajesEnviadosReceptores.json"; //<usuarioReceptor,Collection<idMensaje>>
    public static final String MENSAJES_PENDIENTES_RECEPTORES_FILE_PATH =
        "IdMensajesPendientesReceptores.json"; //<usuarioReceptor,Collection<idMensaje>>
    public static final String MENSAJES_CON_COMPROBANTE_EMISORES_FILE_PATH =
        "IdMensajesConComprobanteEmisores.json"; //<nombreEmisor, <Collection<idMensaje> >
    public static final String COMPROBANTES_SIN_ENVIAR_FILE_PATH = "Comprobantes_Sin_Enviar.json"; //<Emisor,Collection<Comprobante>>


    RuntimeTypeAdapterFactory<Mensaje> factory =
        RuntimeTypeAdapterFactory.of(Mensaje.class, "tipo") // actually type is the default field to determine
                                                                          // the sub class so not needed to set here
                                                                          // but set just to point that it is used
                                                                          // assuming value 1 in field "int type" identifies TextMessage
                                                                          .registerSubtype(MensajeConComprobante.class,
                                                                                           "conComprobante")
                                                                          .registerSubtype(Mensaje.class, "normal") //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
                                                                          // and assuming int 2 identifies ImageMessage
                                                                          .registerSubtype(MensajeConAlerta.class, "conAlerta");
                                                                          
    
    private Gson gson = new GsonBuilder().registerTypeAdapterFactory(factory)
                                         .setPrettyPrinting()
                                         .create();


    private HashMap<Integer, Mensaje> mensajes;
    private HashMap<String, Collection<Integer>> idMensajesEntregadosRecep;
    private HashMap<String, Collection<Integer>> idMensajesEntregarRecep;
    private HashMap<String, Collection<Integer>> idMensajesConComprobEmisores;
    private HashMap<Emisor,Collection<Comprobante>> comprobantesNoEnviados;//<Emisor,Collection<Comprobante>>


    private Integer proximoIdMensaje;


    public PersistenciaMensajesServidorJSON() {
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
        try {
            String json = new String(Files.readAllBytes(Paths.get(MENSAJES_FILE_PATH)), StandardCharsets.UTF_8);
            Type mapType = new TypeToken<HashMap<Integer, Mensaje>>() {
            }.getType();
            mensajes = this.gson.fromJson(json, mapType);
            if (mensajes == null) //se fija si es null porque puede pasar si el archivo existe pero esta vacio
                mensajes = new HashMap<Integer, Mensaje>();
        } catch (IOException e) {
            mensajes = new HashMap<Integer, Mensaje>();
        }
    }

    private void cargaInicialIdMensajesEntregadosRecep() {
        try {
            String json =
                new String(Files.readAllBytes(Paths.get(MENSAJES_ENVIADOS_RECEPTORES_FILE_PATH)),
                           StandardCharsets.UTF_8);
            Type mapType = new TypeToken<HashMap<String, Collection<Integer>>>() {
            }.getType();
            idMensajesEntregadosRecep = this.gson.fromJson(json, mapType);
            if (idMensajesEntregadosRecep ==
                null) //se fija si es null porque puede pasar si el archivo existe pero esta vacio
                idMensajesEntregadosRecep = new HashMap<String, Collection<Integer>>();
        } catch (IOException e) {
            idMensajesEntregadosRecep = new HashMap<String, Collection<Integer>>();
        }
    }

    private void cargaInicialIdMensajesEntregarRecep() {
        try {
            String json =
                new String(Files.readAllBytes(Paths.get(MENSAJES_PENDIENTES_RECEPTORES_FILE_PATH)),
                           StandardCharsets.UTF_8);
            Type mapType = new TypeToken<HashMap<String, Collection<Integer>>>() {
            }.getType();
            idMensajesEntregarRecep = this.gson.fromJson(json, mapType);
            if (idMensajesEntregarRecep ==
                null) //se fija si es null porque puede pasar si el archivo existe pero esta vacio
                idMensajesEntregarRecep = new HashMap<String, Collection<Integer>>();
        } catch (IOException e) {
            idMensajesEntregarRecep = new HashMap<String, Collection<Integer>>();
        }
    }

    private void cargaInicialidMensajesConComprobEmisores() {
        try {
            String json =
                new String(Files.readAllBytes(Paths.get(MENSAJES_CON_COMPROBANTE_EMISORES_FILE_PATH)),
                           StandardCharsets.UTF_8);
            Type mapType = new TypeToken<HashMap<String, Collection<Integer>>>() {
            }.getType();
            idMensajesConComprobEmisores = this.gson.fromJson(json, mapType);
            if (idMensajesConComprobEmisores ==
                null) //se fija si es null porque puede pasar si el archivo existe pero esta vacio
                idMensajesConComprobEmisores = new HashMap<String, Collection<Integer>>();
        } catch (IOException e) {
            idMensajesConComprobEmisores = new HashMap<String, Collection<Integer>>();
        }
    }
    
    private void cargaInicialComprobantesNoEnviados(){
        try {
            String json =
                new String(Files.readAllBytes(Paths.get(COMPROBANTES_SIN_ENVIAR_FILE_PATH)),
                           StandardCharsets.UTF_8);
            Type mapType = new TypeToken<HashMap<Emisor, Collection<Comprobante>>>() {
            }.getType();
            comprobantesNoEnviados = this.gson.fromJson(json, mapType);
            if (comprobantesNoEnviados ==
                null) //se fija si es null porque puede pasar si el archivo existe pero esta vacio
                comprobantesNoEnviados = new HashMap<Emisor, Collection<Comprobante>>();
        } catch (IOException e) {
            comprobantesNoEnviados = new HashMap<Emisor, Collection<Comprobante>>();
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
        String json = "";
        FileWriter file;

        synchronized (mensajes) {
            if (!mensajes.containsKey(mensaje.getId())) { //solo se guarda el mensaje 1 vez
                mensajes.put(mensaje.getId(), mensaje);
                json = this.gson.toJson(mensajes);

                synchronized (MENSAJES_FILE_PATH) {
                    file = new FileWriter(MENSAJES_FILE_PATH);
                    file.write(json);
                    file.close();
                }
            }
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

                json = gson.toJson(idMensajesEntregarRecep);

                synchronized (MENSAJES_PENDIENTES_RECEPTORES_FILE_PATH) {

                    file = new FileWriter(MENSAJES_PENDIENTES_RECEPTORES_FILE_PATH);
                    file.write(json);
                    file.close();
                }
            }


        }

        if (mensaje instanceof MensajeConComprobante) {
            //System.out.println("se esta guardando un mensaje CON comprobante");
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

                json = gson.toJson(idMensajesConComprobEmisores);
                synchronized (MENSAJES_CON_COMPROBANTE_EMISORES_FILE_PATH) {
                    file = new FileWriter(MENSAJES_CON_COMPROBANTE_EMISORES_FILE_PATH);
                    file.write(json);
                    file.close();
                }

            }


        }

    }

    @Override
    public void guardarComp(Comprobante comprobante) throws Exception {
        String json = "";
        FileWriter file;

        MensajeConComprobante mensaje;
        synchronized (mensajes) {
            mensaje = (MensajeConComprobante) mensajes.get(comprobante.getidMensaje());
            mensaje.addReceptorConfirmado(comprobante.getUsuarioReceptor());
            mensajes.put(mensaje.getId(), mensaje);

            json = gson.toJson(mensajes);
            synchronized (MENSAJES_FILE_PATH) {
                file = new FileWriter(MENSAJES_FILE_PATH);
                file.write(json);
                file.close();
            }
        }


    }

    @Override
    public Collection<Mensaje> obtenerMsjsPendientesReceptor(Receptor receptor) throws Exception {
        Collection<Mensaje> mensajesParaReceptor = new ArrayList<Mensaje>();

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
    public Collection<MensajeConComprobante> obtenerMsjsComprobadosEmisor(Emisor emisor) throws Exception {
        Collection<MensajeConComprobante> mensajesComprobados = new ArrayList<MensajeConComprobante>();
        String nombreEmisor = emisor.getNombre();
        synchronized (idMensajesConComprobEmisores) {
            Collection<Integer> idMensajesComprobadosAct = idMensajesConComprobEmisores.get(nombreEmisor);
            if (idMensajesComprobadosAct != null) { //si tiene mensajes con comprobante
                synchronized (mensajes) {
                    for (int id : idMensajesComprobadosAct) {
                        //System.out.println(mensajes.get(id));
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
    public void marcarMensajeEnviado(Mensaje mensaje, String usuarioReceptor, boolean primerIntento) throws Exception {
        String json;
        FileWriter file;

        if (!primerIntento) { //si estaba marcado para entregar hay que sacar
            synchronized (idMensajesEntregarRecep) {
                Collection<Integer> idMensajesRecibidos;
                idMensajesRecibidos =
                    idMensajesEntregarRecep.remove(usuarioReceptor); //usuario receptor existia porque no es el primer intento
                idMensajesRecibidos.remove(mensaje.getId());
                idMensajesEntregarRecep.put(usuarioReceptor, idMensajesRecibidos);

                json = gson.toJson(idMensajesEntregarRecep);

                synchronized (MENSAJES_PENDIENTES_RECEPTORES_FILE_PATH) {

                    file = new FileWriter(MENSAJES_PENDIENTES_RECEPTORES_FILE_PATH);
                    file.write(json);
                    file.close();
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

            json = gson.toJson(idMensajesEntregadosRecep);

            synchronized (MENSAJES_ENVIADOS_RECEPTORES_FILE_PATH) {

                file = new FileWriter(MENSAJES_ENVIADOS_RECEPTORES_FILE_PATH);
                file.write(json);
                file.close();
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
    public void guardarComprobanteNoEnviado(Comprobante comprobante) throws Exception {
        String json;
        FileWriter file;
        
        synchronized(comprobantesNoEnviados){  
            
            Collection<Comprobante> coleccion = this.comprobantesNoEnviados.get(comprobante.getEmisorOriginal());
            if(coleccion==null){
                coleccion = new ArrayList<Comprobante>();
                this.comprobantesNoEnviados.put(comprobante.getEmisorOriginal(),coleccion);
            }
                    
            
            coleccion.add(comprobante);
            
            json = gson.toJson(comprobantesNoEnviados);
            
            synchronized (COMPROBANTES_SIN_ENVIAR_FILE_PATH) {
                file = new FileWriter(COMPROBANTES_SIN_ENVIAR_FILE_PATH);
                file.write(json);
                file.close();
            }
        }
    }

    @Override
    public Collection<Comprobante> getComprobantesNoEnviados(Emisor emisor) {
        synchronized(comprobantesNoEnviados){
            return this.comprobantesNoEnviados.get(emisor);
        }
    }

    @Override
    public void eliminarComprobantesNoEnviados(Emisor emisor) throws Exception {
        String json;
        FileWriter file;
        synchronized(comprobantesNoEnviados){
            this.comprobantesNoEnviados.get(emisor).clear();
            
            json = gson.toJson(comprobantesNoEnviados);
            
            synchronized (COMPROBANTES_SIN_ENVIAR_FILE_PATH) {
                file = new FileWriter(COMPROBANTES_SIN_ENVIAR_FILE_PATH);
                file.write(json);
                file.close();
            }
        }
        
        
    }
}

