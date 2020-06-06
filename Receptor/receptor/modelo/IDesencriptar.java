package receptor.modelo;

import emisor.modelo.Mensaje;

import java.security.PrivateKey;

public interface IDesencriptar {
    public Mensaje desencriptar(Mensaje mensaje,PrivateKey privateKey);
    
}
