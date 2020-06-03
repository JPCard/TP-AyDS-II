package emisor.modelo;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface IEncriptacion {
    
     public Mensaje encriptar(Mensaje mensaje, PublicKey publicKey);
}
