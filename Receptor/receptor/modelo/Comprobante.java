package receptor.modelo;


import emisor.modelo.Emisor;

import java.io.Serializable;

public class Comprobante implements Serializable {
    private int idMensaje;
    private String usuarioReceptor;
    private Emisor emisorOriginal;

    //para serializacion xml
    public Comprobante(){
        
    }

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

    //para serializacion xml
    public void setIdMensaje(int idMensaje) {
        this.idMensaje = idMensaje;
    }

    public void setUsuarioReceptor(String usuarioReceptor) {
        this.usuarioReceptor = usuarioReceptor;
    }

    public void setEmisorOriginal(Emisor emisorOriginal) {
        this.emisorOriginal = emisorOriginal;
    }
}
