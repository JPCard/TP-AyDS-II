package servidormensajeria.persistencia;

import emisor.modelo.Emisor;
import emisor.modelo.Mensaje;
import emisor.modelo.MensajeConComprobante;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

public class PersistenciaParametrosServidor implements IPersistenciaParametrosServidor {
    public static final String PARAMETROS_FILE_PATH = "ParametrosSistemaMensajeria.json";
    private boolean cargado = false;
    private String ipDirectorio;
    private int puertoDirectorioDest;
    private int puertoDirectorioTiempo;
    private String idMetodoPersistencia;
    
    
    public PersistenciaParametrosServidor() {
        super();
    }
    
    public void cargarJSON(String ubicacion) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        String text = new String(Files.readAllBytes(Paths.get(ubicacion)), StandardCharsets.UTF_8);
        
        JSONObject obj = (JSONObject) parser.parse(text);
        
        this.ipDirectorio = obj.get("IPDirectorio").toString();
        this.puertoDirectorioDest = Integer.parseInt(obj.get("PuertoDirectorioDest").toString());            
        this.puertoDirectorioTiempo = Integer.parseInt(obj.get("PuertoDirectorioTiempo").toString());
        this.idMetodoPersistencia = obj.get("MetodoPersistencia").toString();
        this.cargado = true;
    }


    @Override
    public String cargarIPDirectorio() throws Exception {
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        
        return this.ipDirectorio;
    }

    @Override
    public int cargarPuertoDirectorioDestinatarios() throws Exception {
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        
        return this.puertoDirectorioDest;
    }

    @Override
    public int cargarPuertoDirectorioTiempoUltModif() throws Exception {
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        

        return this.puertoDirectorioTiempo;
    }

    @Override
    public String cargarMetodoPersistenciaMsjs() throws Exception{
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        
        return this.idMetodoPersistencia;
    }



}
