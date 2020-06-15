package receptor.modelo;

import emisor.modelo.IDatosEmisor;

import java.io.Serializable;

public interface IComprobante extends Serializable {
    int getidMensaje();

    String getUsuarioReceptor();

    IDatosEmisor getEmisorOriginal();

    void setIdMensaje(int idMensaje);

    void setUsuarioReceptor(String usuarioReceptor);

    void setEmisorOriginal(IDatosEmisor emisorOriginal);
}
