package emisor.modelo;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class EncriptacionRSA implements IEncriptacion {
    
    
    private String asHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;

        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10) {
                strbuf.append("0");
            }

            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }

        return strbuf.toString();
    }
    
    private String encriptaString(String s,PublicKey publicKey) throws NoSuchAlgorithmException,
                                                                        NoSuchPaddingException, InvalidKeyException,
                                                                        IllegalBlockSizeException, BadPaddingException {
        Cipher cipher;
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        //Adding data to the cipher
        byte[] stringBytes = s.getBytes();    
        cipher.update(stringBytes);
        byte[] bytesEncriptados = cipher.doFinal();
        
        return asHex(bytesEncriptados);
        
        
    }
    @Override
    public Mensaje encriptar(Mensaje mensaje, PublicKey publicKey) {
       
        try {
            
            mensaje.setAsunto(encriptaString(mensaje.getAsunto(),publicKey));
            
            
            mensaje.setCuerpo(encriptaString(mensaje.getCuerpo(),publicKey));
            
            
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
