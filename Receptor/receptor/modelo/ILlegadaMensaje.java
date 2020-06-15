package receptor.modelo;

import emisor.modelo.Mensaje;
import emisor.modelo.MensajeConAlerta;
import emisor.modelo.MensajeConComprobante;

public interface ILlegadaMensaje {
    void arriboMensajeSimple(Mensaje mensaje);
    void arriboMensajeConAlerta(MensajeConAlerta mensajeConAlerta);
    void arriboMensajeConComprobante(MensajeConComprobante mensajeConComprobante);
}
