package servidormensajeria.persistencia;

import emisor.modelo.Emisor;
import emisor.modelo.Mensaje;

import emisor.modelo.MensajeConComprobante;

import java.io.IOException;

import java.util.Iterator;

import org.json.simple.parser.ParseException;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

public interface IPersistenciaMensajesServidor {


    public void guardarMsj(Mensaje mensaje) throws Exception;

    public void guardarComp(Comprobante comprobante) throws Exception;

    public Iterator<Mensaje> cargarMsjsPendientesReceptor(Receptor receptor) throws Exception;

    public Iterator<MensajeConComprobante> cargarMsjsPendientesEmisor(Emisor emisor) throws Exception;

    
}
