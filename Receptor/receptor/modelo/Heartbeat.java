package receptor.modelo;

public class Heartbeat {
    private Receptor receptor;
    private boolean retransmitido = false;
    
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
