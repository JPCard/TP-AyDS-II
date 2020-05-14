package receptor.modelo;


import emisor.modelo.Emisor;

import java.io.Serializable;

public class Comprobante implements Serializable {
    private int idMensaje;
    private String usuarioReceptor;
    private Emisor emisorOriginal;

    public Comprobante(int idMensaje, String usuarioReceptor, Emisor emisorOriginal) {
        this.idMensaje = idMensaje;
        this.usuarioReceptor = usuarioReceptor;
      this.emisorOriginal  = emisorOriginal;

    }

    public int getidMensaje() {
        return idMensaje;
    }


    public String getUsuarioReceptor() {
        return usuarioReceptor;
    }


    public Emisor getEmisorOriginal() {
        return emisorOriginal;
    }
}
