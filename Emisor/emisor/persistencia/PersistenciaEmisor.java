package emisor.persistencia;

import emisor.modelo.Agenda;
import emisor.modelo.DatosEmisor;
import emisor.modelo.IDatosEmisor;

import emisor.modelo.IDatosEmisor;

import java.beans.XMLDecoder;

import java.beans.XMLEncoder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;

import java.nio.file.Files;

import java.nio.file.Paths;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.nio.charset.StandardCharsets;

import org.json.simple.parser.ParseException;

public class PersistenciaEmisor implements IPersistenciaEmisor {
    public static final String PARAMETROS_FILE_PATH = "ParametrosEmisor.json";
    private IDatosEmisor emisor;
    private boolean cargado = false;
    private String ipDirectorio;
    private int puertoDirectorioDest;
    private int puertoDirectorioTiempo;
    private String ipServidorMensajeria;
    private int puertoServidorMensajeria;
    private int puertoServidorSolicitarMensajesEmisor;
    private String ipDirectorioSecundario;
    private int puertoDirectorioSecundarioTiempo;
    private int puertoDirectorioSecundarioDest;


    public void cargarJSON(String ubicacion) throws IOException, ParseException {
        JSONParser parser = new JSONParser();

            
            String text = new String(Files.readAllBytes(Paths.get(ubicacion)), StandardCharsets.UTF_8);
            
            JSONObject obj2 = (JSONObject) parser.parse(text);
            
            this.puertoDirectorioDest = Integer.parseInt(obj2.get("PuertoDirectorioDest").toString());            
            this.puertoDirectorioTiempo = Integer.parseInt(obj2.get("PuertoDirectorioTiempo").toString());
            this.puertoServidorSolicitarMensajesEmisor = Integer.parseInt(obj2.get("PuertoServidorSolicitarMensajesEmisor").toString());
            
            this.ipDirectorio = obj2.get("IPDirectorio").toString();
            
            
            this.ipDirectorioSecundario = obj2.get("DirectorioSecundario_IP").toString(); 
            this.puertoDirectorioSecundarioDest = Integer.parseInt(obj2.get("DirectorioSecundario_PuertoDest").toString());   
            this.puertoDirectorioSecundarioTiempo = Integer.parseInt(obj2.get("DirectorioSecundario_PuertoTiempo").toString());   
            
            IDatosEmisor emisor = new DatosEmisor(Integer.parseInt(obj2.get("PuertoPropio").toString()),obj2.get("IPPropia").toString(),obj2.get("Nombre").toString());
            this.emisor = emisor;
            
            this.ipServidorMensajeria = obj2.get("IPServidorMensajeria").toString();
            this.puertoServidorMensajeria = Integer.parseInt(obj2.get("PuertoServidorMensajeria").toString());
            
            this.cargado=true;
    }

    @Override
    public IDatosEmisor cargarEmisor() throws IOException, ParseException {
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        

        return this.emisor;
    }

    @Override
    public String cargarIPDirectorio() throws IOException, ParseException {
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        
        
        
        return this.ipDirectorio;
    }


    @Override
    public int cargarPuertoDirectorioDest() throws  IOException, ParseException {
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        
        return this.puertoDirectorioDest;
    }


    @Override
    public String cargarIPServidorMensajeria() throws IOException, ParseException {
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        return this.ipServidorMensajeria;
    }

    @Override
    public int cargarPuertoServidorMensajeria() throws IOException, ParseException {
            if(!this.cargado)
                this.cargarJSON(PARAMETROS_FILE_PATH);
            return this.puertoServidorMensajeria;
    }

    @Override
    public int cargarPuertoDirectorioTiempo() throws IOException, ParseException {
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        
        return this.puertoDirectorioTiempo;
    }

    @Override
    public int cargarPuertoServidorSolicitarMensajesEmisor() throws IOException, ParseException{
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        
        return this.puertoServidorSolicitarMensajesEmisor;
    }

    @Override
    public String cargarIPDirectorioSecundario() throws Exception {
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        return this.ipDirectorioSecundario;
    }

    @Override
    public int cargarPuertoDirectorioSecundarioTiempo() throws Exception {
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        return this.puertoDirectorioSecundarioTiempo;
    }

    @Override
    public int cargarPuertoDirectorioSecundarioDest() throws Exception {
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        return this.puertoDirectorioSecundarioDest;
    }
}
