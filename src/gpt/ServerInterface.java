package gpt;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerInterface extends Remote {
    String join(PeerInfo peerInfo) throws RemoteException;
    List<PeerInfo> search(String filename, String requesterPeerName) throws RemoteException;
    String update(String filename) throws RemoteException;
}
