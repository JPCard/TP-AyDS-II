package receptor.persistencia;

import emisor.modelo.Emisor;

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
import java.nio.file.Paths;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import receptor.modelo.Receptor;

public class PersistenciaReceptor implements IPersistenciaReceptor {
    private static final String PARAMETROS_FILE_PATH = "ParametrosReceptor.json";
    private boolean cargado = false;
    private String ipDirectorio;
    private int puertoDirectorioRegistro;
    private int puertoDirectorioConexion;
    private String ipServidorMensajeria;
    private int puertoServidorMensajeria;
    private Receptor receptor;


    public PersistenciaReceptor() {
        super();
    }

    public void cargarJSON(String ubicacion) {
        JSONParser parser = new JSONParser();

        try {

            String text = new String(Files.readAllBytes(Paths.get(ubicacion)), StandardCharsets.UTF_8);

            JSONObject obj2 = (JSONObject) parser.parse(text);

            this.puertoDirectorioConexion = Integer.parseInt(obj2.get("PuertoDirectorioConexion").toString());
            this.ipServidorMensajeria = obj2.get("IPServidorMensajeria").toString();
            this.puertoServidorMensajeria = Integer.parseInt(obj2.get("PuertoServidorMensajeria").toString());
            this.ipDirectorio = obj2.get("IPDirectorio").toString();

            Receptor receptor =
                new Receptor(obj2.get("IPPropia").toString(), Integer.parseInt(obj2.get("PuertoPropio").toString()),
                             obj2.get("Nombre").toString(), obj2.get("Usuario").toString());
            this.receptor = receptor;
            System.out.println(receptor.descripcionCompleta());

            this.cargado = true;
        } catch (IOException e) {
            System.out.println("ERROR DE I/O En carga de emisor");
        } catch (ParseException e) {
            System.out.println("ERROR dE PARSEO");
        }

    }


    @Override
    public Receptor cargarReceptor() throws FileNotFoundException {
        if (!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        return this.receptor;
    }

    @Override
    public String cargarIPDirectorio() throws FileNotFoundException {
        if (!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        return this.ipDirectorio;
    }

    @Override
    public int cargarPuertoConexion() throws FileNotFoundException {
        if (!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);

        return this.puertoDirectorioConexion;
    }

    @Override
    public String cargarIPServidorMensajeria() {
        if (!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        
        
        return this.ipServidorMensajeria;
    }

    @Override
    public int cargarPuertoServidorMensajeria() {
        if (!this.cargado)
            this.cargarJSON(PARAMETROS_FILE_PATH);
        
        return this.puertoServidorMensajeria;
    }
}
