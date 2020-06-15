package servidormensajeria.persistencia;

import emisor.modelo.IDatosEmisor;
import emisor.modelo.IMensaje;
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


import receptor.modelo.IComprobante;
import receptor.modelo.Receptor;

public class PersistenciaParametrosServidor implements IPersistenciaParametrosServidor {
    public static final String PARAMETROS_FILE_PATH = "ParametrosSistemaMensajeria.json";
    private boolean cargado = false;
    private String ipDirectorio;
    private int puertoDirectorioDest;
    private int puertoDirectorioTiempo;
    private String idMetodoPersistencia;
    private int puertoRecepcionMensajes;
    private int puertoComprobantes;
    private int puertoDevolverMensajesEmisores;
    private int puertoInfoDirectorioPrincipal;
    
    
    private String ipDirectorioSecundario;
    private int puertoDirectorioSecundarioDest;
    private int puertoDirectorioSecundarioTiempo;
    private int puertoDirectorioSecundarioInfo;


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
        this.puertoRecepcionMensajes = Integer.parseInt(obj.get("PuertoRecepcionMensajes").toString());
        this.puertoComprobantes = Integer.parseInt(obj.get("PuertoComprobantes").toString());
        this.puertoDevolverMensajesEmisores = Integer.parseInt(obj.get("PuertoDevolverMensajesEmisores").toString());
        this.puertoInfoDirectorioPrincipal = Integer.parseInt(obj.get("PuertoInfoDirectorio").toString());
        
        
        
        this.ipDirectorioSecundario = obj.get("DirectorioSecundario_IP").toString();
        this.puertoDirectorioSecundarioDest = Integer.parseInt(obj.get("DirectorioSecundario_PuertoDest").toString());      
        this.puertoDirectorioSecundarioTiempo = Integer.parseInt(obj.get("DirectorioSecundario_PuertoTiempo").toString());      
            this.puertoDirectorioSecundarioInfo = Integer.parseInt(obj.get("DirectorioSecundario_PuertoInfo").toString());      
        
        
        
        
        
        
        
        
        
        
        
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


    @Override
    public int cargarPuertoRecepcionMensajes() throws Exception {
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        
        return this.puertoRecepcionMensajes;
    }

    @Override
    public int cargarPuertoComprobantes() throws Exception {
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        
        return this.puertoComprobantes;
    }

    @Override
    public int cargarPuertoDevolverMensajesEmisores() throws Exception {
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        
        return this.puertoDevolverMensajesEmisores;
    }

    @Override
    public int cargarpuertoDirectorioPrincipalPushReceptores() throws Exception {
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        
        return this.puertoInfoDirectorioPrincipal;
    }
    
    @Override
    public int cargarPuertoDirectorioSecundarioPushReceptores() throws Exception {
        if(!this.cargado)
        this.cargarJSON(PARAMETROS_FILE_PATH);
        return this.puertoDirectorioSecundarioInfo;
    }
    

    @Override
    public String cargarIPDirectorioSecundario()throws Exception  {
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        return this.ipDirectorioSecundario;
    }

    @Override
    public int cargarPuertoDirectorioSecundarioTiempo()throws Exception  {
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        return this.puertoDirectorioSecundarioTiempo;
    }

    @Override
    public int cargarPuertoDirectorioSecundarioDestinatarios() throws Exception {
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        return this.puertoDirectorioSecundarioDest;
    }
}
