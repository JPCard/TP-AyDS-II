package emisor.modelo;

import java.beans.XMLDecoder;

import java.io.BufferedInputStream;

import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.Serializable;

import java.util.ArrayList;

import java.util.Iterator;

import java.util.TreeSet;

import receptor.modelo.IDatosReceptor;

public class Agenda implements Serializable{
    private TreeSet<IDatosReceptor> contactos = new TreeSet<IDatosReceptor>();
    
    public Agenda() {
    }

    public void setContactos(TreeSet<IDatosReceptor> contactos) {
        this.contactos = contactos;
    }

    public TreeSet<IDatosReceptor> getContactos() {
        return contactos;
    }


    /**
     * Post: se carga la agenda que estaba guardada si es que la habia.
     */
   
    
    public Iterator<IDatosReceptor> getIteratorContactos(){
        return contactos.iterator();
    }
    
}
