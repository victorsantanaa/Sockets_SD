package rmi.client;

import rmi.Hora;
import rmi.ServicoHora;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Cliente {

    public static void main(String[] args) throws Exception {
        Registry reg = LocateRegistry.getRegistry();
        ServicoHora shc = (ServicoHora) reg.lookup("rmi://127.0.0.1/servicohora");

        Hora hr = shc.obterHora("Client1");
        System.out.println(hr.nomeCliente +" "+ hr.timeStamp);
    }
}
