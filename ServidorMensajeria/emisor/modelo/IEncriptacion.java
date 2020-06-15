package emisor.modelo;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface IEncriptacion {
    
     public IMensaje encriptar(IMensaje mensaje, PublicKey publicKey);
}
