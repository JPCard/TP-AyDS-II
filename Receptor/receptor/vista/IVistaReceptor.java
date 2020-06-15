package receptor.vista;

import emisor.modelo.IMensaje;

public interface IVistaReceptor {
    public void activarAlerta();
    public void agregarMensaje(IMensaje mensaje);
    public void mostrarErrorNoReceptor();

    public void updateConectado(boolean estado);
    
}
