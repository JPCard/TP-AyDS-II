package servidormensajeria.persistencia;


public class PersistenciaMensajesFactory extends AbstractPersistenciaMensajesFactory {
    private static AbstractPersistenciaMensajesFactory instance = null;

    private PersistenciaMensajesFactory() {
    }

    public static AbstractPersistenciaMensajesFactory getInstance(){
        if(instance == null)
            instance = new PersistenciaMensajesFactory();
        return instance;
    }

    @Override
    public IPersistenciaMensajesServidor crearMetodoPersistenciaMensajesXML() {
        return new PersistenciaMensajesServidorXML();
    }

    @Override
    public IPersistenciaMensajesServidor crearMetodoPersistenciaMensajesJSON() {
        return new PersistenciaMensajesServidorJSON();
    }
}
