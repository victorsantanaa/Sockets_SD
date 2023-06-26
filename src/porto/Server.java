package porto;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server extends UnicastRemoteObject implements PeerServer {
    private Map<String, List<Peer>> fileToPeersMap;

    protected Server() throws RemoteException {
        super();
        fileToPeersMap = new HashMap<>();
    }

    @Override
    public String requestJoin(Peer peer) throws RemoteException {
        for (String file : peer.filenames) {
            fileToPeersMap.computeIfAbsent(file, k -> new ArrayList<>()).add(peer);
        }
        return "JOIN_OK";
    }

    @Override
    public List<Peer> requestSearch(String filename) throws RemoteException {
        return fileToPeersMap.getOrDefault(filename, new ArrayList<>());
    }

    @Override
    public String requestUpdate(Peer peer, String filename) throws RemoteException {
        fileToPeersMap.computeIfAbsent(filename, k -> new ArrayList<>()).add(peer);
        return "UPDATE_OK";
    }

    public static void main(String[] args) {
        try {
            // Criando a instância do servidor
            Server server = new Server();

            // Criando o registro RMI
            Registry registry = LocateRegistry.createRegistry(1099); // Porta padrão para o RMI

            // Ligando o servidor ao registro
            registry.bind("Server", server);

            System.out.println("Servidor pronto para receber requisições!");
        } catch (Exception e) {
            System.out.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }
}

