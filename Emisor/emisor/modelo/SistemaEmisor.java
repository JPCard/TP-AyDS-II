package emisor.modelo;

import directorio.modelo.DirectorioMain;

import emisor.controlador.ControladorEmisor;

import emisor.modelo.AbstractMensajeFactory.TipoMensaje;

import emisor.persistencia.IPersistenciaEmisor;
import emisor.persistencia.PersistenciaEmisor;

import emisor.red.TCPDestinatariosRegistrados;
import emisor.red.TCPdeEmisor;

import java.io.FileNotFoundException;

import java.io.StringWriter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import java.util.TreeSet;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

//jsoneando
import java.io.FileReader;
import java.io.IOException;

import java.security.PublicKey;

import java.util.Collections;

import org.json.simple.*;
import org.json.simple.parser.ParseException;

public class SistemaEmisor {
    private Emisor emisor;
    private TCPdeEmisor tcpdeEmisor;
    private IEncriptacion encriptacion;
    private static SistemaEmisor instance;

    private HashMap<Integer, Mensaje> mensajesEnviados = new HashMap<Integer, Mensaje>();
    private HashMap<Integer, MensajeConComprobante> mensajesConComprobante =
        new HashMap<Integer, MensajeConComprobante>();
    private HashMap<Integer,Mensaje> mensajesNoEnviados;
   
    private IPersistenciaEmisor persistencia = new PersistenciaEmisor();
    

    private SistemaEmisor() throws Exception {
        super();
        encriptacion = new EncriptacionRSA();
        emisor = persistencia.cargarEmisor();

        this.tcpdeEmisor =
            new TCPdeEmisor(persistencia.cargarIPServidorMensajeria(), persistencia.cargarPuertoServidorMensajeria(),
                            persistencia.cargarPuertoServidorSolicitarMensajesEmisor());


    }

    public static void inicializar() throws Exception {
        if (instance == null) {
            instance = new SistemaEmisor();
        }

        Thread hiloenviar = new Thread(instance.tcpdeEmisor);
        hiloenviar.start();
        Thread hiloDestinatarios =
            new Thread(new TCPDestinatariosRegistrados(instance.persistencia.cargarIPDirectorio(),
                                                       instance.persistencia.cargarPuertoDirectorioTiempo(),
                                                       instance.persistencia.cargarPuertoDirectorioDest()));
        hiloDestinatarios.start();

        System.out.println("Hilos de red comenzados");

    }


    public static SistemaEmisor getInstance() {
        return instance;
    }


    public Emisor getEmisor() {
        return emisor;
    }

    public TCPdeEmisor getTcpdeEmisor() {
        return tcpdeEmisor;
    }

    public boolean enviarMensaje(String asunto, String cuerpo, ArrayList<String> usuariosReceptores,
                                 TipoMensaje tipoMensaje) {

        TreeSet<Receptor> contactos = this.getEmisor()
                                          .getAgenda()
                                          .getContactos();

        ArrayList<Receptor> contactosArr = new ArrayList(contactos);

        ArrayList<Mensaje> mensajesCifrados = new ArrayList<Mensaje>();
        ArrayList<Mensaje> mensajesPreCifrado = new ArrayList<Mensaje>();
        for (String receptorActual : usuariosReceptores) {
            Mensaje mensajeCifrado =
                MensajeFactory.getInstance()
                .crearMensaje(this.emisor, asunto, cuerpo, tipoMensaje, usuariosReceptores, receptorActual);

            Mensaje mensajePreCifrado =
                MensajeFactory.getInstance()
                .crearMensaje(this.emisor, asunto, cuerpo, tipoMensaje, usuariosReceptores, receptorActual);

            this.getEmisor()
                .getAgenda()
                .getContactos();

            int indice =
                Collections.binarySearch(contactosArr,
                                         new Receptor("123213", 12312, "AAAAAAAAAA", receptorActual,
                                                      null)); //este receptor es de mentirita y solo para comparar


            PublicKey publicKey = contactosArr.get(indice).getLlavePublica();

            mensajesPreCifrado.add(mensajePreCifrado);
            
            mensajesCifrados.add(this.encriptacion.encriptar(mensajeCifrado, publicKey));
        }
        
        boolean logroEnviar = this.getTcpdeEmisor().enviarMensaje(mensajesPreCifrado,mensajesCifrados);


        

        if (!logroEnviar) {
            
            for(Mensaje mensajeCifrado: mensajesCifrados){
                
                this.guardarMensajeNoEnviado(mensajeCifrado);
            }

        }

        return logroEnviar;
    }
    
    public void guardarMensajeNoEnviado(Mensaje mensajeCifrado){
        synchronized(mensajesNoEnviados){
            mensajesNoEnviados.put(mensajeCifrado.getId(),mensajeCifrado);
        }
        //todo algo de persistencia
    }
    

    public void guardarMensaje(Mensaje mensaje) {

        if (mensaje instanceof MensajeConComprobante) {
            System.out.println(mensaje);
            mensajesConComprobante.put(mensaje.getId(), (MensajeConComprobante) mensaje);
            ControladorEmisor.getInstance().agregarMensajeConComprobante((MensajeConComprobante) mensaje);
        }
        this.mensajesEnviados.put(mensaje.getId(), mensaje);
        //todo algo de persistencia
    }

    public Iterator<Receptor> consultarAgenda() {
        return this.emisor.consultarAgenda();
    }

    public Iterator<MensajeConComprobante> getMensajesConComprobanteIterator() {

        return this.mensajesConComprobante
                   .values()
                   .iterator();
    }

    public void agregarComprobante(Comprobante comprobante) {
        //System.out.println("me llamaron");
        int idMensaje = comprobante.getidMensaje();
        MensajeConComprobante m = mensajesConComprobante.get(idMensaje);
        m.addReceptorConfirmado(comprobante.getUsuarioReceptor());

    }



    public int getPuerto() {
        return this.getEmisor().getPuerto();
    }

    public boolean isComprobado(MensajeConComprobante mensajeSeleccionado, String usuarioReceptor) {

        int id = mensajeSeleccionado.getId();
        System.out.println("la id a buscar es: " + id);
        return mensajesConComprobante.get(id)
                                     .getReceptoresConfirmados()
                                     .contains(usuarioReceptor);

        //        ArrayList<String> receptoresConfirmados = null;
        //        synchronized (listasReceptoresConfirmados) {
        //            receptoresConfirmados = this.listasReceptoresConfirmados.get(mensajeSeleccionado.getId());
        //        }
        //        if (receptoresConfirmados == null)
        //            return false;
        //        else
        //            return receptoresConfirmados.contains(usuarioReceptor);
        //

    }

    public void setAgenda(Collection<Receptor> destinatariosRegistrados) {
        Agenda agenda = new Agenda();
        TreeSet<Receptor> contactosT = new TreeSet<Receptor>(destinatariosRegistrados);


        agenda.setContactos(contactosT);

        this.getEmisor().setAgenda(agenda);
    }


}
