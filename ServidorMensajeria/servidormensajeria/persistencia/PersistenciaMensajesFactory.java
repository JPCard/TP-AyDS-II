package servidormensajeria.persistencia;


public class PersistenciaMensajesFactory extends AbstractPersistenciaMensajesFactory {
    private static AbstractPersistenciaMensajesFactory instance = null;

    private PersistenciaMensajesFactory() {
    }

    public static synchronized  AbstractPersistenciaMensajesFactory getInstance(){
        if(instance == null)
            instance = new PersistenciaMensajesFactory();
        return instance;
    }

    @Override
    protected IPersistenciaMensajesServidor crearMetodoPersistenciaMensajesXML() {
        return new PersistenciaMensajesServidorXML();
    }

    @Override
    protected IPersistenciaMensajesServidor crearMetodoPersistenciaMensajesJSON() {
        return new PersistenciaMensajesServidorJSON();
    }
}
