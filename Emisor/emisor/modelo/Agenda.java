package emisor.modelo;

import java.beans.XMLDecoder;

import java.io.BufferedInputStream;

import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.Serializable;

import java.util.ArrayList;

import java.util.Iterator;

import receptor.modelo.Receptor;

public class Agenda implements Serializable{
    private ArrayList<Receptor> contactos = new ArrayList<Receptor>();
    
    public Agenda() {
    }

    public void setContactos(ArrayList<Receptor> contactos) {
        this.contactos = contactos;
    }

    public ArrayList<Receptor> getContactos() {
        return contactos;
    }


    /**
     * Post: se carga la agenda que estaba guardada si es que la habia.
     */
   
    
    public Iterator<Receptor> getIteratorContactos(){
        return contactos.iterator();
    }
    
}
