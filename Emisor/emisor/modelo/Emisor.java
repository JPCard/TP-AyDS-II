package emisor.modelo;

import emisor.modelo.MensajeFactory.TipoMensaje;

import emisor.persistencia.Agenda;

import java.util.Iterator;

import receptor.modelo.Receptor;

public class Emisor {
    private String IP; //{id}
    private int puerto;
    private String nombre;
    private Agenda agenda;

    public Emisor(int puerto, String IP, String nombre) {
        this.IP = IP;
        this.puerto = puerto;
        this.nombre = nombre;
        this.agenda = new Agenda();
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
