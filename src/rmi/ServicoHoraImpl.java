package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServicoHoraImpl extends UnicastRemoteObject implements ServicoHora  {

    public ServicoHoraImpl() throws RemoteException {
        super();
    }

    @Override
    public Hora obterHora(String nomeCliente) throws RemoteException {
        return new Hora(nomeCliente, System.currentTimeMillis());
    }
}
