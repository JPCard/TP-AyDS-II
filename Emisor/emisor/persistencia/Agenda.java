package emisor.persistencia;

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
    public static final String AGENDA_FILE_PATH = "Agenda.xml";
    
    public Agenda() {
        //todo borrar
        
        
        
        
        //todo borrar hasta aca
        cargarAgenda();
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
    private void cargarAgenda(){
        contactos.clear();
        try {
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(AGENDA_FILE_PATH)));
            contactos = (ArrayList<Receptor>) decoder.readObject();
        } 
        catch (FileNotFoundException e) {
            System.out.println("uy");
            //si no estaba el archivo no carga nada
        }
        catch (ClassCastException e) {
            System.out.println("ay");
            //si no estaba guardado un objeto tipo ArrayList<Receptor> en el archivo no hace nada
        }
    }
    
    public Iterator<Receptor> getIteratorContactos(){
        return contactos.iterator();
    }
    
}
