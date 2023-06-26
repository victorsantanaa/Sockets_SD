package napster;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerRemoteImpl extends UnicastRemoteObject implements ServerRemote {

    public ServerRemoteImpl () throws RemoteException{
        super();
    }

    @Override
    public String obterInfoServidor(String nomeCliente) throws RemoteException {
        System.out.println("Método: obterInfoServidor");
        return "SERVIDOR_NAME";
    }

    @Override
    public String join(String[] arquivos) throws RemoteException {
        return null;
    }
}
