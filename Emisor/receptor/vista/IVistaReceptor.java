package receptor.vista;

import emisor.modelo.Mensaje;

public interface IVistaReceptor {
    public void activarAlerta();
    public void mostrarMensaje(Mensaje mensaje);
    public void mostrarErrorNoReceptor();
}
