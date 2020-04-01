package receptor.modelo;

import emisor.modelo.Mensaje;

import java.io.Serializable;

public class Comprobante implements Serializable {
    private int idMensaje;
    private Receptor receptor;


    public Comprobante(int idMensaje, Receptor receptor) {
        this.idMensaje = idMensaje;
        this.receptor = receptor;
    }

    public int getidMensaje() {
        return idMensaje;
    }

    public Receptor getReceptor() {
        return receptor;
    }




}
