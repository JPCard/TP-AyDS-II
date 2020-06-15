package emisor.red;

import emisor.modelo.IMensaje;

import java.util.Collection;

public interface IEnvioMensaje {
    /**
     * Pre: hay una relacion 1:1 entre mensajesPreCifrado y Post, solo llegan hasta aca para recibir una ID
     * @param mensajesPreCifrado
     * @param mensajesPostCifrado
     * @return
     */
    boolean enviarMensaje(Collection<IMensaje> mensajesPreCifrado, Collection<IMensaje> mensajesPostCifrado);
}
