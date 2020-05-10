package emisor.persistencia;

import emisor.modelo.Agenda;
import emisor.modelo.Emisor;

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
    private Emisor emisor;
    private boolean cargado = false;
    private String ipDirectorio;
    private int puertoDirectorio;
    private String ipServidorMensajeria;
    private int puertoServidorMensajeria;


    public void cargarJSON(String ubicacion) {
        JSONParser parser = new JSONParser();

        try {
            
            String text = new String(Files.readAllBytes(Paths.get(ubicacion)), StandardCharsets.UTF_8);
            
            JSONObject obj2 = (JSONObject) parser.parse(text);
            
            this.puertoDirectorio = Integer.parseInt(obj2.get("PuertoDirectorio").toString());
            this.ipDirectorio = obj2.get("IPDirectorio").toString();
            
            Emisor emisor = new Emisor(Integer.parseInt(obj2.get("PuertoPropio").toString()),obj2.get("IPPropia").toString(),obj2.get("Nombre").toString());
            this.emisor = emisor;
            
            this.ipServidorMensajeria = obj2.get("IPServidorMensajeria").toString();
            this.puertoServidorMensajeria = Integer.parseInt(obj2.get("PuertoServidorMensajeria").toString());
            
            this.cargado=true;
        } catch (IOException e) {
            System.out.println("ERROR DE I/O En carga de emisor");
        } catch (ParseException e) {
            System.out.println("ERROR dE PARSEO");
        }

    }

    @Override
    public Emisor cargarEmisor() throws FileNotFoundException {
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        

        return this.emisor;
    }

    @Override
    public String cargarIPDirectorio() throws FileNotFoundException {
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        
        
        
        return this.ipDirectorio;
    }


    @Override
    public int cargarPuertoDirectorio() throws FileNotFoundException {
        if(!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        
        return this.puertoDirectorio;
    }



}
