package rmi.server;

import rmi.ServicoHora;
import rmi.ServicoHoraImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Servidor {

    public static void main(String[] args) throws Exception {

        ServicoHora sh = new ServicoHoraImpl();

        LocateRegistry.createRegistry(1099);

        Registry reg = LocateRegistry.getRegistry();

        reg.bind("rmi://127.0.0.1/servicohora", sh);
        System.out.println("Servidor no ar");
    }
}
