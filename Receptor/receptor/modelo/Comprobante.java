package receptor.modelo;


import emisor.modelo.Emisor;

import java.io.Serializable;

public class Comprobante implements Serializable {
    private int idMensaje;
    private Receptor receptor;
    private Emisor emisorOriginal;


    public Comprobante(int idMensaje, Receptor receptor, Emisor emisorOriginal) {
        this.idMensaje = idMensaje;
        this.receptor = receptor;
        this.emisorOriginal  = emisorOriginal;
    }

    public int getidMensaje() {
        return idMensaje;
    }

    public Receptor getReceptor() {
        return receptor;
    }


    public Emisor getEmisorOriginal() {
        return emisorOriginal;
    }
}
