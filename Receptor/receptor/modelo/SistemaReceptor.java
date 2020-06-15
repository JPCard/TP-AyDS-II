package receptor.modelo;

import directorio.modelo.IDirectorio;

import directorio.modelo.DirectorioMain;

import emisor.modelo.IDatosEmisor;

import emisor.modelo.IMensaje;

import emisor.modelo.Mensaje;
import emisor.modelo.MensajeConAlerta;
import emisor.modelo.MensajeConComprobante;

import java.io.FileNotFoundException;

import java.security.PrivateKey;

import receptor.controlador.ControladorReceptor;

import receptor.persistencia.IPersistenciaReceptor;
import receptor.persistencia.PersistenciaReceptor;

import receptor.red.IEnvioComprobante;
import receptor.red.TCPMensajeListener;
import receptor.red.TCPHeartbeat;
import receptor.red.TCPEnvioComprobante;

public class SistemaReceptor implements ISistemaReceptor,ILlegadaMensaje {
    private IDatosReceptor receptor;
    private IEnvioComprobante envioComprobante;
    private IPersistenciaReceptor persistencia = new PersistenciaReceptor();
    private PrivateKey llavePrivada;

    private IDesencriptar desencriptador;
    
    public SistemaReceptor() throws FileNotFoundException {
        super();
        this.receptor = persistencia.cargarReceptor();
        this.llavePrivada = persistencia.cargarLlavePrivada();
        this.envioComprobante = new TCPEnvioComprobante(persistencia.cargarIPServidorMensajeria(),persistencia.cargarPuertoServidorMensajeria());
        this.desencriptador = new DesencriptarRSA();
        
        Thread hilo = new Thread(new TCPMensajeListener(this));
        hilo.start();
        Thread hiloConexionDirectorio =
            new Thread(new TCPHeartbeat(this,persistencia.cargarIPDirectorio(),
                                        persistencia.cargarPuertoConexion(),
                                        persistencia.cargarIPDirectorioSecundario(),
                                        persistencia.cargarPuertoSecundarioConexion()));
        hiloConexionDirectorio.start();

    }
    

    public PrivateKey getLlavePrivada() {
        return llavePrivada;
    }

    public IEnvioComprobante getTcpdeReceptor() {
        return envioComprobante;
    }


    public IDatosReceptor getReceptor() {
        return receptor;
    }




    public int getPuerto() {
        return this.receptor.getPuerto();
    }

    public String getUsuarioReceptor() {
        return this.receptor.getUsuario();
    }

    @Override
    public void enviarComprobante(IComprobante comprobante, IDatosEmisor emisor) {
        this.envioComprobante.enviarComprobante(comprobante, emisor);

    }

    @Override
    public void arriboMensaje(IMensaje mensaje) {
        PrivateKey privateKey = this.getLlavePrivada();
        mensaje.onLlegada(this);
        ControladorReceptor.getInstance().mostrarMensaje(this.desencriptador.desencriptar(mensaje,privateKey ));
        
    }

    @Override
    public void arriboMensajeSimple(Mensaje mensaje) {
        //nada
    }

    @Override
    public void arriboMensajeConAlerta(MensajeConAlerta mensajeConAlerta) {
        ControladorReceptor.getInstance().activarAlerta();
    }

    @Override
    public void arriboMensajeConComprobante(MensajeConComprobante mensajeConComprobante) {
        IComprobante comprobante = new Comprobante(mensajeConComprobante.getId(),mensajeConComprobante.getReceptorObjetivo(),mensajeConComprobante.getEmisor());

        ControladorReceptor.getInstance().enviarComprobante(comprobante,mensajeConComprobante.getEmisor());
    }
}
