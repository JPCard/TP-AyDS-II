package directorio.persistencia;

import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PersistenciaDirectorio implements IPersistenciaDirectorio {
    private static final String PARAMETROS_FILE_PATH = "ParametrosDirectorio.json";
    private String ipServidorMensajeria;
    private boolean cargado = false;
    private int puertoPushReceptores;
    private int puertoRecibeGetDestinatarios;
    private int puertoRecibeHeartbeats;
    private int puertoRecibeGetUltimoCambio;
    
    //Parametros para doble directorio
    private String ipOtroDirectorio;
    private int otroDirectorioRecibeHeartbeats;
    private int otroDirectorioRecibeGetDestinatarios;
    private int otroDirectorioUltimoCambio;

    public PersistenciaDirectorio() {
        super();
    }

    @Override
    public String cargarIPOtroDirectorio()throws Exception{
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        return ipOtroDirectorio;
    }
    
    @Override
    public int cargarPuertoHeartbeatsOtroDirectorio()throws Exception{
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        return this.otroDirectorioRecibeHeartbeats;
    }
    
    @Override
    public int cargarPuertoGetDestinatariosOtroDirectorio() throws Exception{ //estos nombres del mal podrian cambiar
    if(!this.cargado)
        this.cargarJSON(PARAMETROS_FILE_PATH);
    
    return this.otroDirectorioRecibeGetDestinatarios;
    }

    @Override
    public String cargarIPServidorMensajeria() throws Exception {
        if (!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);


        return this.ipServidorMensajeria;
    }

    @Override
    public int cargarPuertoServidorMensajeria() throws Exception {
        if (!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);


        return this.puertoPushReceptores;
    }

    private void cargarJSON(String ubicacion)throws IOException, ParseException {
        JSONParser parser = new JSONParser();


        String text = new String(Files.readAllBytes(Paths.get(ubicacion)), StandardCharsets.UTF_8);

        JSONObject obj2 = (JSONObject) parser.parse(text);

        this.ipServidorMensajeria = obj2.get("IPServidorMensajeria").toString();
           this.puertoPushReceptores = Integer.parseInt(obj2.get("PuertoPushReceptores").toString());
           this.puertoRecibeHeartbeats = Integer.parseInt(obj2.get("PuertoRecibeHeartbeats").toString());
           this.puertoRecibeGetDestinatarios = Integer.parseInt(obj2.get("PuertoRecibeGetDestinatarios").toString());
           this.puertoRecibeGetUltimoCambio = Integer.parseInt(obj2.get("PuertoRecibeGetUltimoCambio").toString());
           
        this.ipOtroDirectorio = obj2.get("IPOtroDirectorio").toString();
        this.otroDirectorioRecibeHeartbeats = Integer.parseInt(obj2.get("OtroDirec_RecibeHeartbeats").toString());
        this.otroDirectorioRecibeGetDestinatarios = Integer.parseInt(obj2.get("OtroDirec_RecibeGetDestinatarios").toString());
        this.otroDirectorioUltimoCambio = Integer.parseInt(obj2.get("OtroDirec_UltimoCambio").toString());

        cargado = true;
    }

    @Override
    public int cargarPuertoRecibeHeartbeats() throws Exception {
        if (!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);


        return this.puertoRecibeHeartbeats;
    }

    @Override
    public int cargarPuertoRecibeGetDestinatarios() throws Exception {
        if (!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);


        return this.puertoRecibeGetDestinatarios;
    }

    @Override
    public int cargarPuertoRecibeGetUltimoCambio() throws Exception {
        if (!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);


        return this.puertoRecibeGetUltimoCambio;
    }

    @Override
    public int cargarpuertoPushReceptores() throws Exception {
        if (!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);


        return this.puertoPushReceptores;
    }

    @Override
    public int cargarPuertoUltimoCambioOtroDirectorio() throws Exception{
        if (!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        
        
        return this.otroDirectorioUltimoCambio;
    }
}
