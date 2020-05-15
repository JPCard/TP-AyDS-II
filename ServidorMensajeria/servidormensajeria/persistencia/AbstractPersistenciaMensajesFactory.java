package servidormensajeria.persistencia;

import servidormensajeria.modelo.SistemaServidor;

public abstract class AbstractPersistenciaMensajesFactory {
    public static final String METODO_PERSIST_XML = "XML";
    public static final String METODO_PERSIST_JSON = "JSON";
    

    /**
     * Pre: metodo != null
     * @param metodo
     * @return
     */
    public IPersistenciaMensajesServidor crearMetodoPersistenciaMensajes(String metodo){
        switch (metodo) {
        case METODO_PERSIST_JSON:
            return crearMetodoPersistenciaMensajesJSON();
        case METODO_PERSIST_XML:
            return crearMetodoPersistenciaMensajesXML();
        default:
            return null;
        }
    }
    
    public abstract IPersistenciaMensajesServidor crearMetodoPersistenciaMensajesXML();
    
    public abstract IPersistenciaMensajesServidor crearMetodoPersistenciaMensajesJSON();
    
    
    
}
