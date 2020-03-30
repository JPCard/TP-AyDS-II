package receptor.modelo;

import emisor.modelo.Mensaje;

public class Comprobante {
    private Mensaje mensaje;
    private Receptor receptor;


    public Comprobante(Mensaje mensaje, Receptor receptor) {
        this.mensaje = mensaje;
        this.receptor = receptor;
    }

    public Mensaje getMensaje() {
        return mensaje;
    }

    public Receptor getReceptor() {
        return receptor;
    }




}
