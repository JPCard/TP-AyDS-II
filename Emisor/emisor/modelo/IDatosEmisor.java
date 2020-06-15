package emisor.modelo;

import java.io.Serializable;

import java.util.Iterator;

import receptor.modelo.Receptor;

public interface IDatosEmisor extends Cloneable, Serializable {
    @Override
    boolean equals(Object object);

    @Override
    int hashCode();

    void setIP(String IP);

    @Override
    String toString();

    void setPuerto(int puerto);

    void setNombre(String nombre);

    void setAgenda(Agenda agenda);

    Agenda getAgenda();

    String getIP();

    int getPuerto();

    String getNombre();

    Iterator<Receptor> consultarAgenda();
}
