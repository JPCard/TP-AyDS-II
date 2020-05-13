package receptor.modelo;


import java.io.Serializable;

public class Comprobante implements Serializable {
    private int idMensaje;
    private String usuarioReceptor;


    public Comprobante(int idMensaje, String usuarioReceptor) {
        this.idMensaje = idMensaje;
        this.usuarioReceptor = usuarioReceptor;
    }

    public int getidMensaje() {
        return idMensaje;
    }


    public String getUsuarioReceptor() {
        return usuarioReceptor;
    }


}
