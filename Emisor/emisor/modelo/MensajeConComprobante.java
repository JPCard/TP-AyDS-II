package emisor.modelo;

import java.io.Serializable;

import java.util.ArrayList;

import java.util.Collection;

import receptor.controlador.ControladorReceptor;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;
import receptor.modelo.SistemaReceptor;

public class MensajeConComprobante extends Mensaje implements Serializable {
    private Collection<String> receptoresConfirmados;
    
    public MensajeConComprobante(Emisor emisor, String asunto, String cuerpo,ArrayList<String> receptores, String receptorObjetivo) {
        super(emisor, asunto, cuerpo,receptores,receptorObjetivo);
        receptoresConfirmados = new  ArrayList<String>(); //por el momento no hay nadie confirmado
    }
    
    public MensajeConComprobante(){
        super();
    } //para serializacion

    /**
     *Pre: el mensaje tiene id seteado
     */
    @Override
    public void onLlegada() {
        super.onLlegada();
        Comprobante comprobante = new Comprobante(this.getId(),ControladorReceptor.getInstance().getReceptor().getUsuario(),this.getEmisor());

        ControladorReceptor.getInstance().enviarComprobante(comprobante,this.getEmisor());
    }
    
    @Override
    public IMensaje clone() {
        IMensaje m = MensajeFactory.getInstance()
               .crearMensaje(this.getEmisor(), this.getAsunto(), this.getCuerpo(),
                             AbstractMensajeFactory.TipoMensaje.MSJ_CON_COMPROBANTE, this.getUsuariosReceptores(),
                             this.getReceptorObjetivo());
        m.setId(this.getId());
        return m;
    }
    
    @Override
    public String toString() {
        return "IMensaje con Comprobante\n"+super.toString();
    }

    /**
     * Pre:receptor tiene no esta agregado a la lista de receptores confirmados, receptor esta en la lista de receptores
     */
    public void addReceptorConfirmado(String usuarioReceptor){
        receptoresConfirmados.add(usuarioReceptor);
    }


    public void setReceptoresConfirmados(Collection<String> receptoresConfirmados) {
        this.receptoresConfirmados = receptoresConfirmados;
    }

    public Collection<String> getReceptoresConfirmados() {
        return receptoresConfirmados;
    }


}
