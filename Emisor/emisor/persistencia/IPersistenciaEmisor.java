package emisor.persistencia;

import emisor.modelo.Emisor;

public interface IPersistenciaEmisor {
    public Emisor cargarEmisor();
    public Agenda cargarAgenda();
}
