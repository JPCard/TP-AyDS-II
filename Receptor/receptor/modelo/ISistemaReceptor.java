package receptor.modelo;

import emisor.modelo.IDatosEmisor;

import emisor.modelo.IMensaje;

import java.security.PrivateKey;

import receptor.red.IEnvioComprobante;

public interface ISistemaReceptor {
    PrivateKey getLlavePrivada();

    IEnvioComprobante getTcpdeReceptor();

    IDatosReceptor getReceptor();

    int getPuerto();

    String getUsuarioReceptor();

    public void enviarComprobante(IComprobante comprobante, IDatosEmisor emisor);

    public void arriboMensaje(IMensaje mensaje);
}
