package porto;

import java.io.Serializable;
import java.util.List;

public class Peer implements Serializable {
    public String ip;
    public int port;

    public Peer(String ip, int port, List<String> filenames) {
        this.ip = ip;
        this.port = port;
        this.filenames = filenames;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<String> getFilenames() {
        return filenames;
    }

    public void setFilenames(List<String> filenames) {
        this.filenames = filenames;
    }

    public List<String> filenames;

}
