package emisor.vista;

import java.util.ArrayList;

import receptor.modelo.Receptor;

public interface IVistaEmisor {
    public String getAsunto();
    public String getCuerpo();
    public ArrayList<Receptor> getDestinatarios();

    public void mostrarErrorEmisorContactos();
}
