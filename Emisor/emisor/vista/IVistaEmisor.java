package emisor.vista;

import java.util.ArrayList;

import java.util.Collection;

import receptor.modelo.Receptor;

public interface IVistaEmisor {
    public String getAsunto();
    public String getCuerpo();
    public ArrayList<Receptor> getDestinatarios();
    public void mostrarErrorEmisorContactos();

    public void updateConectado(boolean estado);

    public void cargarContactos(Collection<Receptor> destinatariosRegistrados);

    public void mostrarErrorServidorNoDisponible();
    public void envioExitoso();
}
