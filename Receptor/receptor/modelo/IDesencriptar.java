package receptor.modelo;

import emisor.modelo.IMensaje;

import java.security.PrivateKey;

public interface IDesencriptar {
    public IMensaje desencriptar(IMensaje mensaje,PrivateKey privateKey);
    
}
