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

import org.json.simple.*;
import org.json.simple.parser.ParseException;

public class SistemaEmisor {
    private Emisor emisor;
    private TCPdeEmisor tcpdeEmisor;

    private static SistemaEmisor instance;

    private HashMap<Integer, Mensaje> mensajesEnviados = new HashMap<Integer, Mensaje>();
    private HashMap<Integer, MensajeConComprobante> mensajesConComprobante =
        new HashMap<Integer, MensajeConComprobante>();

    private HashMap<Integer, ArrayList<String>> listasReceptoresConfirmados = //ahora los identificamos con su usario
        new HashMap<Integer, ArrayList<String>>();
    private IPersistenciaEmisor persistencia = new PersistenciaEmisor();

    private SistemaEmisor() throws Exception {
        super();
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

        Mensaje mensaje =
            MensajeFactory.getInstance().crearMensaje(this.emisor, asunto, cuerpo, tipoMensaje, usuariosReceptores);


        return this.getTcpdeEmisor().enviarMensaje(mensaje);
    }

    public void guardarMensaje(Mensaje mensaje) {

        if (mensaje instanceof MensajeConComprobante) {
            mensajesConComprobante.put(mensaje.getId(), (MensajeConComprobante) mensaje);
            ControladorEmisor.getInstance().agregarMensajeConComprobante((MensajeConComprobante) mensaje);
        }


        this.mensajesEnviados.put(mensaje.getId(), mensaje);
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
        System.out.println("me llamaron");
        int idMensaje = comprobante.getidMensaje();
        MensajeConComprobante m = mensajesConComprobante.get(idMensaje);
        m.addReceptorConfirmado(comprobante.getUsuarioReceptor());
        synchronized (mensajesConComprobante) {
            //            System.out.println(mensajesConComprobante);
            System.out.println("llegue al synch");
            //            System.out.println("FIN MENsaJES CON COMPROBANTE");
            if (this.mensajesConComprobante.containsKey(idMensaje)) {
                System.out.println("contengo");

                //
                synchronized (listasReceptoresConfirmados) {
                    if (!listasReceptoresConfirmados.containsKey(idMensaje))
                        listasReceptoresConfirmados.put(idMensaje,
                                                        new ArrayList<String>()); //si es el primer comprobante, crea el arraylist


                    this.listasReceptoresConfirmados
                        .get(idMensaje)
                        .add(comprobante.getUsuarioReceptor());
                }
            }
        }
        //else
    }

    public Iterator<String> getReceptoresConfirmados(Mensaje mensaje) {
        synchronized (listasReceptoresConfirmados) {
            return this.listasReceptoresConfirmados
                       .get(mensaje.getId())
                       .iterator();
        }
    }

    public boolean hayReceptoresConfirmados(Mensaje mensaje) {
        synchronized (listasReceptoresConfirmados) {
            return this.listasReceptoresConfirmados.containsKey(mensaje.getId());
        }
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

    public void inicializarMensajesConComprobante() {
        Collection<MensajeConComprobante> mensajesC = instance.tcpdeEmisor.solicitarMensajesEnviados();
        for (MensajeConComprobante mensaje : mensajesC) {
            instance.guardarMensaje(mensaje);
            instance.mensajesConComprobante.put(mensaje.getId(), (MensajeConComprobante) mensaje);
            ControladorEmisor.getInstance().agregarMensajeConComprobante((MensajeConComprobante) mensaje);
        }
    }
}
