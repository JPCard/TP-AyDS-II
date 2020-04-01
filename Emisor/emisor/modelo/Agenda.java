package emisor.modelo;

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


    
   
    
    public Iterator<Receptor> getIteratorContactos(){
        return contactos.iterator();
    }
    
}
