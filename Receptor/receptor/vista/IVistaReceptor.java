package receptor.vista;

import emisor.modelo.Mensaje;

public interface IVistaReceptor {
    public void activarAlerta();
    public void agregarMensaje(Mensaje mensaje);
    public void mostrarErrorNoReceptor();

    public void updateConectado(boolean estado);
    
}
