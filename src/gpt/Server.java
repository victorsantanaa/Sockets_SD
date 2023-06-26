package gpt;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Server implements ServerInterface {
    private List<PeerInfo> peers;

    public Server() {
        peers = new ArrayList<>();
    }

    public synchronized String join(PeerInfo peerInfo) throws RemoteException {
        peers.add(peerInfo);
        System.out.println( "Peer " + peerInfo.getPeerName() +" adicionado com arquivos " + peerInfo.getFiles().toString());
        return "JOIN_OK";
    }

    public synchronized List<PeerInfo> search(String filename, String requesterPeerName) throws RemoteException {
        System.out.println("Peer " + requesterPeerName + " solicitou arquivo " + filename);
        List<PeerInfo> foundPeers = new ArrayList<>();
        for (PeerInfo peer : peers) {
            if (peer.getFiles().contains(filename)) {
                foundPeers.add(peer);
            }
        }
        return foundPeers;
    }

    public synchronized String update(String filename) throws RemoteException {
        for (PeerInfo peer : peers) {
            if (!peer.getFiles().contains(filename)) {
                peer.getFiles().add(filename);
            }
        }
        return "UPDATE_OK";
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(server, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("server", stub);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
