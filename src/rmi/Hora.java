package rmi;

import java.io.Serializable;

public class Hora implements Serializable {

    public String nomeCliente;
    public long timeStamp;



    public Hora(String nomCli, long timStp) {
        this.nomeCliente = nomCli;
        this.timeStamp = timStp;
    }
}
