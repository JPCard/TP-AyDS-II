package emisor.vista;

import java.util.ArrayList;

import java.util.Iterator;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

public interface IVistaEmisor {
    public String getAsunto();
    public String getCuerpo();
    public ArrayList<Receptor> getDestinatarios();

    public void mostrarErrorEmisorContactos();
}
