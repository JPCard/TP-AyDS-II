package emisor.modelo;

import emisor.modelo.MensajeFactory.TipoMensaje;

import emisor.persistencia.Agenda;

import receptor.modelo.Receptor;

public class Emisor {
    private String IP; //{id}
    private int puerto;
    private String nombre;
    private Agenda agenda;
    private Emisor instance;


    private Emisor(int puerto, String IP, String nombre) {
        this.IP = IP;
        this.puerto = puerto;
        this.nombre = nombre;
        this.agenda = new Agenda();
    }
    
    public Emisor getInstance(int puerto, String IP, String nombre) {
        if (instance == null)
               instance = new Emisor(puerto,IP,nombre);
        return instance;
    }
    
    
    public Mensaje enviarMensaje(String asunto, String cuerpo, Receptor receptor, TipoMensaje tipoMensaje){
        Mensaje mensaje = MensajeFactory.crearMensaje(this, asunto, cuerpo, tipoMensaje);
        return mensaje;
    }
    
    
    
    public String consultarAgenda(){
        return ""; //TODO
    }
    


}