package emisor.vista;

import java.util.ArrayList;

import java.util.Collection;

import receptor.modelo.Receptor;

public interface IVistaEmisor {
    public String getAsunto();
    public String getCuerpo();
    public ArrayList<Receptor> getDestinatarios();
    public void cargarContactos();
    public void mostrarErrorEmisorContactos();

    public void updateConectado(boolean estado);
}
