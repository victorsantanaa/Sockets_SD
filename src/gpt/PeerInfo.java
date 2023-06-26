package gpt;

import java.io.Serializable;
import java.util.List;

public class PeerInfo implements Serializable {
    private String peerName;
    private List<String> files;

    public PeerInfo(String peerName, List<String> files) {
        this.peerName = peerName;
        this.files = files;
    }

    public String getPeerName() {
        return peerName;
    }

    public List<String> getFiles() {
        return files;
    }
}
