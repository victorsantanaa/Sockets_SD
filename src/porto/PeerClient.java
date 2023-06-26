package porto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PeerClient extends Peer {
    private PeerServer server;

    public PeerClient(String ip, int port, List<String> filenames, PeerServer server) {
        super(ip, port, filenames);
        this.server = server;
    }

    public void join() {
        try {
            String response = server.requestJoin(this);
            if (response.equals("JOIN_OK")) {
                System.out.println("Sou peer " + this.ip + ":" + this.port + " com arquivos " + String.join(" ", this.filenames));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void update(String filename) {
        try {
            String response = server.requestUpdate(this, filename);
            if (response.equals("UPDATE_OK")) {
                this.filenames.add(filename);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void search(String filename) {
        try {
            List<Peer> peers = server.requestSearch(filename);
            if (!peers.isEmpty()) {
                System.out.println("Peers com arquivo solicitado: " + peers.stream()
                        .map(peer -> peer.ip + ":" + peer.port)
                        .collect(Collectors.joining(", ")));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public void download(Peer peer, String filename) {
        try (Socket socket = new Socket(peer.ip, peer.port);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            out.writeUTF(filename);

            try (DataInputStream in = new DataInputStream(socket.getInputStream())) {
                long fileSize = in.readLong();
                byte[] buffer = new byte[4096];
                int read;
                long totalRead = 0;
                long remaining = fileSize;

                try (FileOutputStream fos = new FileOutputStream(filename)) {
                    while((read = in.read(buffer, 0, Math.min(buffer.length, (int)remaining))) > 0) {
                        totalRead += read;
                        remaining -= read;
                        fos.write(buffer, 0, read);
                    }

                    System.out.println("Arquivo.java " + filename + " baixado com sucesso na pasta " + System.getProperty("user.dir"));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o IP do peer: ");
        String ip = scanner.nextLine();
        System.out.print("Digite a porta do peer: ");
        int port = scanner.nextInt();
        scanner.nextLine(); // Consumir newline left-over
        System.out.print("Digite os nomes dos arquivos (separados por espaço): ");
        List<String> filenames = Arrays.asList(scanner.nextLine().split(" "));
       // PeerClient peer = new PeerClient(ip, port, filenames);
       // peer.join();
    }

}

