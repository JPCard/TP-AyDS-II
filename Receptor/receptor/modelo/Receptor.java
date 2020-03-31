package receptor.modelo;

import emisor.modelo.Emisor;
import emisor.modelo.Mensaje;

import emisor.modelo.SistemaEmisor;

import emisor.red.TCPdeEmisor;

import java.beans.XMLDecoder;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.Serializable;

import java.util.HashMap;

public class Receptor implements Serializable{
    private String IP;
    private int puerto;
    private String nombre;

    
    public Receptor(String IP, int puerto, String nombre) {
        this.IP = IP;
        this.puerto = puerto;
        this.nombre = nombre;
    }

    public Receptor(){
        super();
    }

    @Override
    public String toString() {
        return this.getNombre();
    }

    public String descripcionCompleta(){
        return "Nombre: "+this.getNombre()+"\nIP: "+this.getIP()+"\nPuerto: "+this.getPuerto();
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    
    
    public String getIP() {
        return IP;
    }

    public int getPuerto() {
        return puerto;
    }

    public String getNombre() {
        return nombre;
    }

}
