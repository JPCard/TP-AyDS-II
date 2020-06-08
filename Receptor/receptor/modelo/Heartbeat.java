package receptor.modelo;

import java.io.Serializable;

public class Heartbeat implements Serializable {
    private Receptor receptor;
    private boolean retransmitido = false;

    public Heartbeat(){
        
    }

    @Override
    public String toString() {
        
        return "heartbeat del receptor "+this.receptor.getUsuario()+" Retransmitido = "+this.retransmitido;
    }

    public void setReceptor(Receptor receptor) {
        this.receptor = receptor;
    }

    public Heartbeat(Receptor receptor) {
        super();
        this.receptor = receptor;
    }

    public void setRetransmitido(boolean retransmitido) {
        this.retransmitido = retransmitido;
    }

    public Receptor getReceptor() {
        return receptor;
    }

    public boolean isRetransmitido() {
        return retransmitido;
    }
}
