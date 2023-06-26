package porto;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface PeerServer extends Remote {
    String requestJoin(Peer peer) throws RemoteException;
    List<Peer> requestSearch(String filename) throws RemoteException;
    String requestUpdate(Peer peer, String filename) throws RemoteException;
}
