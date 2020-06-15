package emisor.red;

public interface IDestinatariosRegistrados extends Runnable {
    static final int TIEMPO_ACTUALIZACION_DESTINATARIOS = 1000;

    long getTiempoUltModif();

    @Override
    void run();
}
