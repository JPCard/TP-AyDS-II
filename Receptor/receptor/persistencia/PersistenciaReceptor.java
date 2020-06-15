package receptor.persistencia;

import emisor.modelo.IDatosEmisor;

import java.beans.XMLDecoder;

import java.beans.XMLEncoder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.FileOutputStream;

import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import java.security.interfaces.RSAPublicKey;

import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import java.security.spec.X509EncodedKeySpec;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import receptor.controlador.ControladorReceptor;

import receptor.modelo.DatosReceptor;
import receptor.modelo.IDatosReceptor;
import receptor.modelo.IDatosReceptor;

public class PersistenciaReceptor implements IPersistenciaReceptor {
    private static final String PARAMETROS_FILE_PATH = "ParametrosReceptor.json";
    private static final String LLAVE_PUBLICA_FILE_PATH = "llavePublica.pub";
    private static final String LLAVE_PRIVADA_FILE_PATH = "llavePrivada";
    private boolean cargado = false;
    private String ipDirectorio;
    private int puertoDirectorioConexion;
    
    private String ipDirectorioSecundario;
    private int puertoDirectorioSecundarioConexion;
    
    
    private String ipServidorMensajeria;
    private int puertoServidorMensajeria;
    private IDatosReceptor receptor;
    private KeyPair llaves;


    public PersistenciaReceptor() {
        super();
    }

    /**
     * Pre: debe ser llamado una sola vez
     * @param ubicacionPrivada
     * @param ubicacionPublica
     */
    public void cargarLlaves(String ubicacionPrivada, String ubicacionPublica) {
        try {
            Path pathPrivada = Paths.get(ubicacionPrivada);
            byte[] bytesPrivada = Files.readAllBytes(pathPrivada);

            //traer privada desde archivo
            PKCS8EncodedKeySpec ksPrivada = new PKCS8EncodedKeySpec(bytesPrivada);
            KeyFactory kfPrivada = KeyFactory.getInstance("RSA");
            PrivateKey pvt = kfPrivada.generatePrivate(ksPrivada);

            Path pathPublica = Paths.get(ubicacionPublica);
            byte[] bytesPublica = Files.readAllBytes(pathPublica);

            //traer publica desde archivo
            X509EncodedKeySpec ksPublica = new X509EncodedKeySpec(bytesPublica);
            KeyFactory kfPublica = KeyFactory.getInstance("RSA");
            PublicKey pub = kfPublica.generatePublic(ksPublica);

            this.llaves = new KeyPair(pub, pvt);

        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println("Generando llaves seguras");
            KeyPairGenerator kpg;
            try {
                kpg = KeyPairGenerator.getInstance("RSA");
                kpg.initialize(2048);
                KeyPair kp = kpg.generateKeyPair();
                this.llaves = kp;

                //guardando las llaves
                FileOutputStream outPub = new FileOutputStream(ubicacionPublica);
                outPub.write(kp.getPublic().getEncoded());
                outPub.close();


                FileOutputStream outPriv = new FileOutputStream(ubicacionPrivada);
                outPriv.write(kp.getPrivate().getEncoded());
                outPriv.close();


            } catch (NoSuchAlgorithmException f) {
                f.printStackTrace();
            } catch (FileNotFoundException f) {
                f.printStackTrace();
            } catch (IOException f) {
                f.printStackTrace();
            }

        }
    }


    /**
     * Pre: Debe haber sido llamado previamente cargarLlaves(String ubicacionPrivada, String ubicacionPublica)
     * @param ubicacionParametros el nombre de archivo JSON donde se encuentran los parametros
     */
    public void cargarJSON(String ubicacionParametros) {
        JSONParser parser = new JSONParser();

        try {

            String text = new String(Files.readAllBytes(Paths.get(ubicacionParametros)), StandardCharsets.UTF_8);

            JSONObject obj2 = (JSONObject) parser.parse(text);

            this.puertoDirectorioConexion = Integer.parseInt(obj2.get("PuertoDirectorioConexion").toString());
            this.ipServidorMensajeria = obj2.get("IPServidorMensajeria").toString();
            this.puertoServidorMensajeria = Integer.parseInt(obj2.get("PuertoServidorMensajeria").toString());
            this.ipDirectorio = obj2.get("IPDirectorio").toString();

            this.ipDirectorioSecundario =obj2.get("DirectorioSecundario_IP").toString();
            this.puertoDirectorioSecundarioConexion = Integer.parseInt(obj2.get("DirectorioSecundario_PuertoConexion").toString());


            IDatosReceptor receptor =
                new DatosReceptor(obj2.get("IPPropia").toString(), Integer.parseInt(obj2.get("PuertoPropio").toString()),
                             obj2.get("Nombre").toString(), obj2.get("Usuario").toString(),this.llaves.getPublic());
            this.receptor = receptor;
            System.out.println("Programa receptor: datos cargados");
            System.out.println(receptor.descripcionCompleta());

            this.cargado = true;
        } catch (IOException e) {
            System.err.println("ERROR DE I/O En carga de receptor");
        } catch (ParseException e) {
            System.err.println("ERROR de parseo en carga de receptor");
        }

    }


    @Override
    public IDatosReceptor cargarReceptor() throws FileNotFoundException {
        if (!this.cargado) {
            this.cargarLlaves(LLAVE_PRIVADA_FILE_PATH, LLAVE_PUBLICA_FILE_PATH);
            this.cargarJSON(PARAMETROS_FILE_PATH);
        }

        return this.receptor;
    }

    @Override
    public String cargarIPDirectorio() throws FileNotFoundException {
        if (!this.cargado) {
            this.cargarLlaves(LLAVE_PRIVADA_FILE_PATH, LLAVE_PUBLICA_FILE_PATH);
            this.cargarJSON(PARAMETROS_FILE_PATH);
        }
        return this.ipDirectorio;
    }

    @Override
    public int cargarPuertoConexion() throws FileNotFoundException {
        if (!this.cargado) {
            this.cargarLlaves(LLAVE_PRIVADA_FILE_PATH, LLAVE_PUBLICA_FILE_PATH);
            this.cargarJSON(PARAMETROS_FILE_PATH);
        }

        return this.puertoDirectorioConexion;
    }

    @Override
    public String cargarIPServidorMensajeria() {
        if (!this.cargado) {
            this.cargarLlaves(LLAVE_PRIVADA_FILE_PATH, LLAVE_PUBLICA_FILE_PATH);
            this.cargarJSON(PARAMETROS_FILE_PATH);
        }


        return this.ipServidorMensajeria;
    }

    @Override
    public int cargarPuertoServidorMensajeria() {
        if (!this.cargado) {
            this.cargarLlaves(LLAVE_PRIVADA_FILE_PATH, LLAVE_PUBLICA_FILE_PATH);
            this.cargarJSON(PARAMETROS_FILE_PATH);
        }

        return this.puertoServidorMensajeria;
    }

    @Override
    public PrivateKey cargarLlavePrivada() {
        if (!this.cargado) {
            this.cargarLlaves(LLAVE_PRIVADA_FILE_PATH, LLAVE_PUBLICA_FILE_PATH);
            this.cargarJSON(PARAMETROS_FILE_PATH);
        }
        return this.llaves.getPrivate();
    }

    @Override
    public String cargarIPDirectorioSecundario() {
        if (!this.cargado) {
            this.cargarLlaves(LLAVE_PRIVADA_FILE_PATH, LLAVE_PUBLICA_FILE_PATH);
            this.cargarJSON(PARAMETROS_FILE_PATH);
        }
        return this.ipDirectorioSecundario;
    }

    @Override
    public int cargarPuertoSecundarioConexion() {
        if (!this.cargado) {
            this.cargarLlaves(LLAVE_PRIVADA_FILE_PATH, LLAVE_PUBLICA_FILE_PATH);
            this.cargarJSON(PARAMETROS_FILE_PATH);
        }
        return this.puertoDirectorioSecundarioConexion;
    }
}
