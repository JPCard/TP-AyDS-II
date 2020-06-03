package receptor.modelo;

import emisor.modelo.Mensaje;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class DesencriptarRSA implements IDesencriptar {
    public DesencriptarRSA() {
        super();
    }

    private byte[] fromHexString(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    
    private String desencriptaString(String s,PrivateKey privateKey) throws NoSuchAlgorithmException,
                                                                             NoSuchPaddingException,
                                                                             InvalidKeyException,
                                                                             IllegalBlockSizeException,
                                                                             BadPaddingException {
        
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytes = cipher.doFinal(this.fromHexString(s));
        return new String(bytes);
    }

    @Override
    public Mensaje desencriptar(Mensaje mensaje, PrivateKey privateKey) {
        try {
            
            
            
            
            mensaje.setAsunto(this.desencriptaString(mensaje.getAsunto(), privateKey));
            
            mensaje.setCuerpo(this.desencriptaString(mensaje.getCuerpo(), privateKey));
            

        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return mensaje;
    }
}
