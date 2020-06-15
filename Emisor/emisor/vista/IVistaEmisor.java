package emisor.vista;

import java.util.ArrayList;

import java.util.Collection;

import receptor.modelo.IDatosReceptor;

public interface IVistaEmisor {
    public String getAsunto();
    public String getCuerpo();
    public ArrayList<IDatosReceptor> getDestinatarios();
    public void mostrarErrorEmisorContactos();

    public void updateConectado(boolean estado);

    public void cargarContactos(Collection<IDatosReceptor> destinatariosRegistrados);

    public void mostrarErrorServidorNoDisponible();
    public void envioExitoso();
    
    
    public void setNombreUsuario(String nombreEmisor);
}
