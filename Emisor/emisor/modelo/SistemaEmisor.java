package emisor.modelo;

import directorio.modelo.DirectorioMain;

import emisor.controlador.ControladorEmisor;

import emisor.modelo.AbstractMensajeFactory.TipoMensaje;

import emisor.persistencia.IPersistenciaEmisor;
import emisor.persistencia.IPersistenciaMensajesEmisor;
import emisor.persistencia.PersistenciaEmisor;

import emisor.persistencia.PersistenciaMensajesEmisorXML;

import emisor.red.IEnvioMensaje;
import emisor.red.RedComprobantes;
import emisor.red.TCPDestinatariosRegistrados;
import emisor.red.TCPMensajesPendientes;
import emisor.red.TCPComprobantes;

import emisor.red.TCPEnvioMensaje;

import java.io.FileNotFoundException;

import java.io.StringWriter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import java.util.TreeSet;

import receptor.modelo.IComprobante;
import receptor.modelo.IDatosReceptor;

//jsoneando
import java.io.FileReader;
import java.io.IOException;

import java.security.PublicKey;

import java.util.Collections;

import org.json.simple.*;
import org.json.simple.parser.ParseException;

import receptor.modelo.DatosReceptor;

public class SistemaEmisor implements ISistemaEmisor {
    private IDatosEmisor emisor;
    private RedComprobantes iRedComprobantes;
    private IEncriptacion encriptacion;

    private IPersistenciaMensajesEmisor persistenciaMensajes;

    private IPersistenciaEmisor persistencia = new PersistenciaEmisor();
    private Thread hiloMensajesPendientes = null;
    private IEnvioMensaje tcpEnvioMensaje;

    private AbstractMensajeFactory mensajeFactory = new MensajeFactory();

    public SistemaEmisor() throws Exception {
        super();
        this.persistenciaMensajes = new PersistenciaMensajesEmisorXML();
        encriptacion = new EncriptacionRSA();
        emisor = persistencia.cargarEmisor();

        this.iRedComprobantes =
            new TCPComprobantes(persistencia.cargarIPServidorMensajeria(), persistencia.cargarPuertoServidorMensajeria(),
                            persistencia.cargarPuertoServidorSolicitarMensajesEmisor(),this);
        
        Thread hiloComprobantes = new Thread(iRedComprobantes);
        hiloComprobantes.start();
        
        this.tcpEnvioMensaje = new TCPEnvioMensaje(persistencia.cargarIPServidorMensajeria(),persistencia.cargarPuertoServidorMensajeria());
        
        
        Thread hiloPedirDestinatarios =
            new Thread(new TCPDestinatariosRegistrados(this,persistencia.cargarIPDirectorio(),
                                                       persistencia.cargarPuertoDirectorioTiempo(),
                                                       persistencia.cargarPuertoDirectorioDest(),
                                                       persistencia.cargarIPDirectorioSecundario(),
                                                       persistencia.cargarPuertoDirectorioSecundarioTiempo(),
                                                       persistencia.cargarPuertoDirectorioSecundarioDest()));
        hiloPedirDestinatarios.start();

        System.out.println("Hilos de red comenzados");

        if (this.persistenciaMensajes.quedanMensajesPendientes())
            this.inciarHiloMensajesPendientes();
    }





    public IDatosEmisor getEmisor() {
        return emisor;
    }

    public RedComprobantes getTcpdeEmisor() {
        return iRedComprobantes;
    }

    public boolean enviarMensaje(String asunto, String cuerpo, ArrayList<String> usuariosReceptores,
                                 TipoMensaje tipoMensaje) {

        TreeSet<IDatosReceptor> contactos = this.getEmisor()
                                          .getAgenda()
                                          .getContactos();

        ArrayList<IDatosReceptor> contactosArr = new ArrayList(contactos);

        ArrayList<IMensaje> mensajesCifrados = new ArrayList<IMensaje>();
        ArrayList<IMensaje> mensajesPreCifrado = new ArrayList<IMensaje>();
        for (String receptorActual : usuariosReceptores) {
            IMensaje mensajeCifrado =
                this.mensajeFactory
                .crearMensaje(this.emisor, asunto, cuerpo, tipoMensaje, usuariosReceptores, receptorActual);

            IMensaje mensajePreCifrado =
            mensajeFactory
                .crearMensaje(this.emisor, asunto, cuerpo, tipoMensaje, usuariosReceptores, receptorActual);

            int indice =
                Collections.binarySearch(contactosArr,
                                         new DatosReceptor("123213", 12312, "AAAAAAAAAA", receptorActual,
                                                      null)); //este receptor es de mentirita y solo para comparar


            PublicKey publicKey = contactosArr.get(indice).getLlavePublica();

            mensajesPreCifrado.add(mensajePreCifrado);

            mensajesCifrados.add(this.encriptacion.encriptar(mensajeCifrado, publicKey));
        }

        boolean logroEnviar = this.tcpEnvioMensaje.enviarMensaje(mensajesPreCifrado, mensajesCifrados);


        if (!logroEnviar) { //aca setear id negativa

            Iterator<IMensaje> itPre = mensajesPreCifrado.iterator();
            Iterator<IMensaje> itPost = mensajesCifrados.iterator();


            while (itPre.hasNext()) {
                //
                IMensaje mensajeCifrado = itPost.next();

                itPre.next().setId(this.persistenciaMensajes.getNextIdNoEnviados());
                mensajeCifrado.setId(this.persistenciaMensajes.getNextIdNoEnviados());

                this.persistenciaMensajes.pasarASiguienteIdNoEnviados();

                this.persistenciaMensajes.guardarMensajeEncriptado(mensajeCifrado);
            }

        }

        //aca guardar
        for (IMensaje mensaje : mensajesPreCifrado) {
            guardarMensaje(mensaje);
        }

        if (!logroEnviar)
            this.inciarHiloMensajesPendientes();

        return logroEnviar;
    }


    public void guardarMensaje(IMensaje mensajeSinEncriptar) {

        if (mensajeSinEncriptar instanceof MensajeConComprobante) {
//            System.out.println(mensajeSinEncriptar);
            //mensajesConComprobante.put(mensajeSinEncriptar.getId(), (MensajeConComprobante) mensaje);
            this.persistenciaMensajes.guardarMensajeConComprobante((MensajeConComprobante) mensajeSinEncriptar);
            ControladorEmisor.getInstance().mostrarMensajeConComprobante((MensajeConComprobante) mensajeSinEncriptar);
        }
    }

    public Iterator<IDatosReceptor> consultarAgenda() {
        return this.emisor.consultarAgenda();
    }

    public Iterator<MensajeConComprobante> getMensajesConComprobanteIterator() {

        return this.persistenciaMensajes
                   .obtenerMsjsComprobadosEmisor()
                   .iterator();
    }

    public void agregarComprobante(IComprobante comprobante) {
        //System.out.println("me llamaron");
        //        int idMensaje = comprobante.getidMensaje();
        //        MensajeConComprobante m = mensajesConComprobante.get(idMensaje);
        //        m.addReceptorConfirmado(comprobante.getUsuarioReceptor());
        this.persistenciaMensajes.guardarComp(comprobante);
        ControladorEmisor.getInstance().agregarComprobante(comprobante);
    }


    public int getPuerto() {
        return this.getEmisor().getPuerto();
    }

    public boolean isComprobado(MensajeConComprobante mensajeSeleccionado, String usuarioReceptor) {

        int id = mensajeSeleccionado.getId();
        //        System.out.println("la id a buscar es: " + id);
        return this.persistenciaMensajes.isComprobado(mensajeSeleccionado, usuarioReceptor);


    }

    public void setAgenda(Collection<IDatosReceptor> destinatariosRegistrados) {
        Agenda agenda = new Agenda();
        TreeSet<IDatosReceptor> contactosT = new TreeSet<IDatosReceptor>(destinatariosRegistrados);


        agenda.setContactos(contactosT);

        this.getEmisor().setAgenda(agenda);
        
        ControladorEmisor.getInstance().setAgenda(destinatariosRegistrados);
    }


    public void cargarComprobantesAsincronicos() {

        Collection<IComprobante> comprobantes = iRedComprobantes.solicitarComprobantesAsincronicos();
        if (comprobantes != null && !comprobantes.isEmpty())
            for (IComprobante comprobante : comprobantes) {
                this.persistenciaMensajes.guardarComp(comprobante);
            }
    }

    public Collection<IMensaje> getMensajesNoEnviados() {
        return this.persistenciaMensajes.getMensajesNoEnviados();
    }


    /**
     * Marca los mensajes que estaban esperando ser enviados. Los marca como enviados.
     */
    public void marcarMensajesPendientesComoEnviados(Collection<IMensaje> mensajesPendientes) {
        this.persistenciaMensajes.marcarMensajesPendientesComoEnviados(mensajesPendientes);
    }

    public boolean quedanMensajesPendientes() {
        return this.persistenciaMensajes.quedanMensajesPendientes();
    }

    public void actualizarIdMensaje(Integer viejaId, Integer nuevaId) {
        this.persistenciaMensajes.actualizarIdMensaje(viejaId, nuevaId);
    }

    private void inciarHiloMensajesPendientes() {
        if (this.hiloMensajesPendientes == null || !this.hiloMensajesPendientes.isAlive()) {
            this.hiloMensajesPendientes =
                new Thread(new TCPMensajesPendientes(iRedComprobantes.getIpServidorMensajeria(),
                                                     iRedComprobantes.getPuertoServidorMensajeria(),this));
            this.hiloMensajesPendientes.start();
        }

    }

    public String getNombreEmisor() {
        return this.emisor.getNombre();
    }

    @Override
    public void updateConectado(boolean estado) {
        ControladorEmisor.getInstance().updateConectado(estado);
    }
}
