package receptor.modelo;

import java.io.Serializable;

import java.security.PublicKey;

public interface IDatosReceptor extends Comparable<IDatosReceptor>, Serializable {
    void setLlavePublica(PublicKey llavePublica);

    PublicKey getLlavePublica();

    @Override
    boolean equals(Object object);

    @Override
    int hashCode();

    void setUsuario(String usuario);

    void setConectado(boolean conectado);

    boolean isConectado();

    String getUsuario();

    @Override
    String toString();

    String descripcionCompleta();

    void setIP(String IP);

    void setPuerto(int puerto);

    void setNombre(String nombre);

    String getIP();

    int getPuerto();

    String getNombre();

    @Override
    int compareTo(IDatosReceptor receptor);
}
