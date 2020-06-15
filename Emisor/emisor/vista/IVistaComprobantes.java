package emisor.vista;


import emisor.modelo.MensajeConComprobante;

import receptor.modelo.IComprobante;

public interface IVistaComprobantes {
    public void agregarMensajeConComprobante(MensajeConComprobante mensaje);
    public void actualizarComprobanteRecibidos(IComprobante comprobante) ;
}
