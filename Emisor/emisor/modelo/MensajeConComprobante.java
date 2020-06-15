package emisor.modelo;

import java.io.Serializable;

import java.util.ArrayList;

import java.util.Collection;

import receptor.controlador.ControladorReceptor;

import receptor.modelo.Comprobante;
import receptor.modelo.IComprobante;
import receptor.modelo.IDatosReceptor;
import receptor.modelo.ILlegadaMensaje;
import receptor.modelo.ISistemaReceptor;
import receptor.modelo.SistemaReceptor;

public class MensajeConComprobante extends Mensaje implements Serializable {
    private Collection<String> receptoresConfirmados;
    
    public MensajeConComprobante(IDatosEmisor emisor, String asunto, String cuerpo,ArrayList<String> receptores, String receptorObjetivo) {
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
    public void onLlegada(ILlegadaMensaje llegadaMensaje) {
        super.onLlegada(llegadaMensaje);
        llegadaMensaje.arriboMensajeConComprobante(this);
    }
    
    @Override
    public IMensaje clone() {
        IMensaje m = new MensajeFactory()
               .crearMensaje(this.getEmisor(), this.getAsunto(), this.getCuerpo(),
                             AbstractMensajeFactory.TipoMensaje.MSJ_CON_COMPROBANTE, this.getUsuariosReceptores(),
                             this.getReceptorObjetivo());
        m.setId(this.getId());
        return m;
    }
    
    @Override
    public String toString() {
        return "IMensaje con IComprobante\n"+super.toString();
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
