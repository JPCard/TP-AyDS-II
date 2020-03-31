package receptor.modelo;

import emisor.modelo.Mensaje;

public class Comprobante {
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
