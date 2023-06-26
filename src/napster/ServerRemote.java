package napster;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRemote extends Remote {
    public String obterInfoServidor(String nomeCliente) throws RemoteException;

    String join(String[] arquivos) throws RemoteException;
}
