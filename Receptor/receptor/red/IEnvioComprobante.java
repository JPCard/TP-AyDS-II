package receptor.red;

import emisor.modelo.IDatosEmisor;

import receptor.modelo.IComprobante;

public interface IEnvioComprobante {
    void enviarComprobante(IComprobante comprobante, IDatosEmisor emisor);
}
