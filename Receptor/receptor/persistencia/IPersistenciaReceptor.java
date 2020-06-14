package receptor.persistencia;

import java.beans.XMLEncoder;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;

import java.io.FileOutputStream;

import java.security.PrivateKey;

import receptor.modelo.Receptor;

public interface IPersistenciaReceptor {
    public Receptor cargarReceptor() throws FileNotFoundException ;
    public String cargarIPDirectorio() throws FileNotFoundException;
    public int cargarPuertoConexion() throws FileNotFoundException;


    public String cargarIPServidorMensajeria();

    public int cargarPuertoServidorMensajeria();
    
    public PrivateKey cargarLlavePrivada();

    public String cargarIPDirectorioSecundario();

    public int cargarPuertoSecundarioConexion();
}
