package receptor.persistencia;

import java.io.FileNotFoundException;

import receptor.modelo.Receptor;

public interface IPersistenciaReceptor {
    public Receptor cargarReceptor() throws FileNotFoundException ;
}
