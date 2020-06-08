package emisor.modelo;

import directorio.modelo.DirectorioMain;

import emisor.controlador.ControladorEmisor;

import emisor.modelo.AbstractMensajeFactory.TipoMensaje;

import emisor.persistencia.IPersistenciaEmisor;
import emisor.persistencia.IPersistenciaMensajesEmisor;
import emisor.persistencia.PersistenciaEmisor;

import emisor.persistencia.PersistenciaMensajesEmisorXML;

import emisor.red.TCPDestinatariosRegistrados;
import emisor.red.TCPMensajesPendientes;
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


    private IPersistenciaMensajesEmisor persistenciaMensajes;

    private IPersistenciaEmisor persistencia = new PersistenciaEmisor();
    private Thread TCPMensajesPendientes = null;


    private SistemaEmisor() throws Exception {
        super();
        this.persistenciaMensajes = new PersistenciaMensajesEmisorXML();
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

        if(instance.persistenciaMensajes.quedanMensajesPendientes())
            instance.inciarHiloMensajesPendientes();

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

            int indice =
                Collections.binarySearch(contactosArr,
                                         new Receptor("123213", 12312, "AAAAAAAAAA", receptorActual,
                                                      null)); //este receptor es de mentirita y solo para comparar


            PublicKey publicKey = contactosArr.get(indice).getLlavePublica();

            mensajesPreCifrado.add(mensajePreCifrado);

            mensajesCifrados.add(this.encriptacion.encriptar(mensajeCifrado, publicKey));
        }

        boolean logroEnviar = this.getTcpdeEmisor().enviarMensaje(mensajesPreCifrado, mensajesCifrados);


        if (!logroEnviar) { //aca setear id negativa

            Iterator<Mensaje> itPre = mensajesPreCifrado.iterator();
            Iterator<Mensaje> itPost = mensajesCifrados.iterator();


            while (itPre.hasNext()) {
                //
                Mensaje mensajeCifrado = itPost.next();

                itPre.next().setId(this.persistenciaMensajes.getNextIdNoEnviados());
                mensajeCifrado.setId(this.persistenciaMensajes.getNextIdNoEnviados());

                this.persistenciaMensajes.pasarASiguienteIdNoEnviados();

                this.persistenciaMensajes.guardarMensajeEncriptado(mensajeCifrado);
            }

        }

        //aca guardar
        for (Mensaje mensaje : mensajesPreCifrado) {
            guardarMensaje(mensaje);
        }

        if (!logroEnviar)
            this.inciarHiloMensajesPendientes();

        return logroEnviar;
    }


    public void guardarMensaje(Mensaje mensajeSinEncriptar) {

        if (mensajeSinEncriptar instanceof MensajeConComprobante) {
            System.out.println(mensajeSinEncriptar);
            //mensajesConComprobante.put(mensajeSinEncriptar.getId(), (MensajeConComprobante) mensaje);
            this.persistenciaMensajes.guardarMensajeConComprobante((MensajeConComprobante) mensajeSinEncriptar);
            ControladorEmisor.getInstance().mostrarMensajeConComprobante((MensajeConComprobante) mensajeSinEncriptar);
        }
    }

    public Iterator<Receptor> consultarAgenda() {
        return this.emisor.consultarAgenda();
    }

    public Iterator<MensajeConComprobante> getMensajesConComprobanteIterator() {

        return this.persistenciaMensajes
                   .obtenerMsjsComprobadosEmisor()
                   .iterator();
    }

    public void agregarComprobante(Comprobante comprobante) {
        //System.out.println("me llamaron");
        //        int idMensaje = comprobante.getidMensaje();
        //        MensajeConComprobante m = mensajesConComprobante.get(idMensaje);
        //        m.addReceptorConfirmado(comprobante.getUsuarioReceptor());
        this.persistenciaMensajes.guardarComp(comprobante);
    }


    public int getPuerto() {
        return this.getEmisor().getPuerto();
    }

    public boolean isComprobado(MensajeConComprobante mensajeSeleccionado, String usuarioReceptor) {

        int id = mensajeSeleccionado.getId();
        //        System.out.println("la id a buscar es: " + id);
        return this.persistenciaMensajes.isComprobado(mensajeSeleccionado, usuarioReceptor);


    }

    public void setAgenda(Collection<Receptor> destinatariosRegistrados) {
        Agenda agenda = new Agenda();
        TreeSet<Receptor> contactosT = new TreeSet<Receptor>(destinatariosRegistrados);


        agenda.setContactos(contactosT);

        this.getEmisor().setAgenda(agenda);
    }


    public void cargarComprobantesAsincronicos() {

        Collection<Comprobante> comprobantes = instance.tcpdeEmisor.solicitarComprobantesAsincronicos();
        if (comprobantes != null && !comprobantes.isEmpty())
            for (Comprobante comprobante : comprobantes) {
                this.persistenciaMensajes.guardarComp(comprobante);
            }
    }

    public Collection<Mensaje> getMensajesNoEnviados() {
        return this.persistenciaMensajes.getMensajesNoEnviados();
    }


    /**
     * Marca los mensajes que estaban esperando ser enviados. Los marca como enviados.
     */
    public void marcarMensajesPendientesComoEnviados(Collection<Mensaje> mensajesPendientes) {
        this.persistenciaMensajes.marcarMensajesPendientesComoEnviados(mensajesPendientes);
    }

    public boolean quedanMensajesPendientes() {
        return this.persistenciaMensajes.quedanMensajesPendientes();
    }

    public void actualizarIdMensaje(Integer viejaId, Integer nuevaId) {
        this.persistenciaMensajes.actualizarIdMensaje(viejaId,nuevaId);
    }

    private void inciarHiloMensajesPendientes() {
        if (this.TCPMensajesPendientes == null || !this.TCPMensajesPendientes.isAlive()) {
            this.TCPMensajesPendientes =
                new Thread(new TCPMensajesPendientes(this.tcpdeEmisor.getIpServidorMensajeria(),
                                                     this.tcpdeEmisor.getPuertoServidorMensajeria()));
            this.TCPMensajesPendientes.start();
        }

    }
}
