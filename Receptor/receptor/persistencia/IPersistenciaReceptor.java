package receptor.persistencia;

import java.beans.XMLEncoder;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;

import java.io.FileOutputStream;

import receptor.modelo.Receptor;

public interface IPersistenciaReceptor {
    public Receptor cargarReceptor() throws FileNotFoundException ;
    public String cargarIPDirectorio() throws FileNotFoundException;
    public int cargarPuertoConexion() throws FileNotFoundException;
    public int cargarPuertoRegistro() throws FileNotFoundException;
    
   
}
