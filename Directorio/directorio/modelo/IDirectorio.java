package directorio.modelo;

import directorio.persistencia.IPersistenciaDirectorio;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

import receptor.modelo.Receptor;

public interface IDirectorio {
    int getPuertoRecibeHeartbeats();

    int getPuertoRecibeGetDestinatarios();

    int getPuertoRecibeGetUltimoCambio();

    IPersistenciaDirectorio getPersistenciaDirectorio();

    int getOtroDirectorioPuertoUltimoCambio();

    void setTiempoUltModif(Long tiempoUltModif);

    String getIpServidorMensajeria();

    int getPuertoPushReceptores();

    /**
     *
     * @return Coleccion ordenada por usuario de receptores
     */
    Collection<Receptor> getReceptores();

    HashMap<String, Long> getTiempos();

    /**
     * Este metodo debe llamarse cuando se agrega un nuevo receptor o cuando se modifica un receptor
     * @param receptor
     */
    void heartbeatRecibido(Receptor receptor);

    Collection<Receptor> listaDestinatariosRegistrados();

    long getTiempoUltModif();

    String getIpOtroDirectorio();

    int getOtroDirectorioPuertoDestinatarios();

    int getOtroDirectorioPuertoHeartbeats();

    void setReceptores(TreeMap<String, Receptor> receptores);
}
