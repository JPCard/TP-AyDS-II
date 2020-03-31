package emisor.vista;

import emisor.modelo.Mensaje;

import emisor.modelo.MensajeConComprobante;

import receptor.modelo.Comprobante;

public interface IVistaComprobantes {
    public void agregarMensajeConComprobante(MensajeConComprobante mensaje);
    public void actualizarComprobanteRecibidos(Comprobante comprobante) ;
}
