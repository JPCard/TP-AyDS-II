package servidormensajeria.persistencia;

import emisor.modelo.Mensaje;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

public interface IPersistenciaServidor {
    
    
    public void guardarMsj(Mensaje mensaje);
    public void guardarComp(Comprobante comprobante);
    public void cargarMsjsPendientesPara(Receptor receptor);
    
}
