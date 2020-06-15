package receptor.modelo;


import emisor.modelo.IDatosEmisor;

import java.io.Serializable;

public class Comprobante implements IComprobante {
    private int idMensaje;
    private String usuarioReceptor;
    private IDatosEmisor emisorOriginal;

    //para serializacion xml
    public Comprobante(){
        
    }

    public Comprobante(int idMensaje, String usuarioReceptor, IDatosEmisor emisorOriginal) {
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


    public IDatosEmisor getEmisorOriginal() {
        return emisorOriginal;
    }

    //para serializacion xml
    public void setIdMensaje(int idMensaje) {
        this.idMensaje = idMensaje;
    }

    public void setUsuarioReceptor(String usuarioReceptor) {
        this.usuarioReceptor = usuarioReceptor;
    }

    public void setEmisorOriginal(IDatosEmisor emisorOriginal) {
        this.emisorOriginal = emisorOriginal;
    }
}
