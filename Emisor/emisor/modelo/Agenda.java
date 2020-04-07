package emisor.modelo;

import java.beans.XMLDecoder;

import java.io.BufferedInputStream;

import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.Serializable;

import java.util.ArrayList;

import java.util.Iterator;

import java.util.TreeSet;

import receptor.modelo.Receptor;

public class Agenda implements Serializable{
    private TreeSet<Receptor> contactos = new TreeSet<Receptor>();
    
    public Agenda() {
    }

    public void setContactos(TreeSet<Receptor> contactos) {
        this.contactos = contactos;
    }

    public TreeSet<Receptor> getContactos() {
        return contactos;
    }


    /**
     * Post: se carga la agenda que estaba guardada si es que la habia.
     */
   
    
    public Iterator<Receptor> getIteratorContactos(){
        return contactos.iterator();
    }
    
}
