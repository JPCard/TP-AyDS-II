package emisor.vista;

import emisor.modelo.Mensaje;

import receptor.modelo.Comprobante;

public interface IVistaComprobantes {
    public void agregarMensajeConComprobante(Mensaje mensaje);
    public void actualizarComprobanteRecibidos(Comprobante comprobante) ;
}
