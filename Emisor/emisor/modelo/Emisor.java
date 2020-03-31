package emisor.modelo;

import emisor.modelo.MensajeFactory.TipoMensaje;

import emisor.persistencia.Agenda;

import java.io.Serializable;

import java.util.Iterator;

import receptor.modelo.Receptor;

public class Emisor implements Serializable{
    private String IP; //{id}
    private int puerto;
    private String nombre;
    private Agenda agenda;

    public Emisor() {
        super();
    }
    
    public Emisor(int puerto, String IP, String nombre) {
        this.IP = IP;
        this.puerto = puerto;
        this.nombre = nombre;
        this.agenda = new Agenda();
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    @Override
    public String toString() {
        return "Nombre: "+this.getNombre()+"\nIP: "+this.getIP()+"\nPuerto: "+this.getPuerto();
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public Agenda getAgenda() {
        return agenda;
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

    


    public Iterator<Receptor> consultarAgenda() {
        return this.agenda.getIteratorContactos();
    }
}
