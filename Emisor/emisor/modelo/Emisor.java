package emisor.modelo;

import emisor.persistencia.Agenda;

import receptor.modelo.Receptor;

public class Emisor {
    private String IP; //{id}
    private int puerto;
    private String nombre;
    private Agenda agenda;
    private Emisor instance;


    private Emisor(int puerto, String IP, String nombre) {
        super();
    }
    
    public Emisor getInstance(int puerto, String IP, String nombre) {
        if (instance == null)
               instance = new Emisor(puerto,IP,nombre);
        return instance;
    }
    
    
    public void enviarMensaje(String asunto, String cuerpo, Receptor receptor){
        
    }
    
    public void enviarMensajeConAlerta(String asunto, String cuerpo, Receptor receptor){
        
    }
    
    public void enviarMensajeConComprobante(String asunto, String cuerpo, Receptor receptor){
        
    }
    
    public String consultarAgenda(){
        return "";
    }
    


}