package gpt;

import porto.Peer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.util.*;

public class Client {
    private ServerInterface server;
    private Path path;

    public InetSocketAddress getI() {
        return i;
    }

    public void setI(InetSocketAddress i) {
        this.i = i;
    }

    private InetSocketAddress i;

    private Path getPath() {
        return path;
    }

    List<String> files = new ArrayList<>();

    public void setPath(String folder) throws IOException {
        path = Paths.get(folder);
        DirectoryStream<Path> stream = Files.newDirectoryStream(path);

        for (Path entry : stream) {
            files.add(String.valueOf(entry.getFileName()));
        }

    }

    public Client() {
        try {
            server = (ServerInterface) Naming.lookup("rmi://localhost:1099/server");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void join(String peerName) {
        try {
            PeerInfo peerInfo = new PeerInfo(peerName, files);
            String response = server.join(peerInfo);
            System.out.println(response);
            if (Objects.equals(response, "JOIN_OK")) {
                System.out.println("Sou o peer " + peerName + " " + files.toString());
            } else {
                System.out.println("JOIN_NOT_OKAY");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void search(String filename, String requesterPeerName) {
        try {
            List<PeerInfo> foundPeers = server.search(filename, requesterPeerName);
            if (foundPeers.isEmpty()) {
                System.out.println("[]");
            } else {
                System.out.println("Peers com arquivo solicitado: ");
                for (PeerInfo peer : foundPeers) {
                    System.out.println(peer.getPeerName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(String filename) {
        try {
            String response = server.update(filename);
            System.out.println("Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void requestDownload(String ip, int port, String fileName) throws IOException, ClassNotFoundException {
        Socket socketSender = new Socket(ip, port);
        DataOutputStream out = new DataOutputStream(socketSender.getOutputStream());
        DataInputStream dataInputStream = new DataInputStream(socketSender.getInputStream());
        FileOutputStream fileOutputStream = new FileOutputStream(path + "\\" + fileName);

        try {
            out.writeUTF(fileName);

            int bytes = 0;
            long size = dataInputStream.readF();         // Le o tamanho do arquivo

/*
            // Count the total bytes
            // form the input stream
            int count = inputStream.available();

            // Create byte array
            byte[] b = new byte[count];

            // Read data into byte array
            int bytes = dataInputStr.read(b);

            */

            byte[] buffer = new byte[1000 * 1024];

            while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                fileOutputStream.write(buffer, 0, bytes);
                size -= bytes;
            }
            System.out.println("Arquivo " + fileName + " baixado com sucesso na pasta " + path);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fecharConxoes(socketSender, out, dataInputStream, fileOutputStream);

        }

    }

    public void download(String ip, int port, String filename) {
        try (Socket socket = new Socket(ip, port);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            out.writeUTF(filename);

            try (DataInputStream in = new DataInputStream(socket.getInputStream())) {
                long fileSize = in.readLong();
                byte[] buffer = new byte[4096];
                int read;
                long totalRead = 0;
                long remaining = fileSize;

                try (FileOutputStream fos = new FileOutputStream(filename)) {
                    while ((read = in.read(buffer, 0, Math.min(buffer.length, (int) remaining))) > 0) {
                        System.out.println(fileSize);
                        totalRead += read;
                        remaining -= read;
                        fos.write(buffer, 0, read);
                        System.out.println(remaining);
                    }

                    System.out.println("Arquivo " + filename + " baixado com sucesso na pasta " + System.getProperty("user.dir"));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void fecharConxoes(Socket socket, DataOutputStream output, DataInputStream input, FileOutputStream fileOut) throws IOException {
        input.close();
        output.close();
        socket.close();
        fileOut.close();
    }

    public static class ClientThread extends Thread {
        private Client client;
        private Scanner scanner;


        public ClientThread(Client client, Scanner scanner) {
            this.client = client;
            this.scanner = scanner;
        }

        @Override
        public void run() {
            try {
                System.out.println("Digite o IP: ");
                String ipClient = scanner.nextLine();
                System.out.println("Digite a porta:");
                String portClient = scanner.nextLine();
                System.out.println("Digite a pasta:");
                String folder = scanner.nextLine();
                client.setPath(folder);

                String peerName = ipClient + ":" + portClient;

                client.setI(new InetSocketAddress(ipClient, Integer.parseInt(portClient)));
                new ListenerThread(client.getI(), client.getPath()).start();

                while (true) {
                    System.out.println("1 - JOIN | 2 - SEARCH | 3 - DOWNLOAD:");
                    int option = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character

                    switch (option) {
                        case 1:
                            client.join(peerName);
                            break;
                        case 2:
                            System.out.println("Digite o nome do arquivo a ser pesquisado:");
                            String filename = scanner.nextLine();
                            client.search(filename, peerName);
                            break;
                        case 3:
                            System.out.println("Digite o IP do Peer: ");
                            String ipDoPeer = scanner.nextLine();
                            System.out.println("Digite a porta do Peer: ");
                            String portaDoPeer = scanner.nextLine();
                            System.out.println("Digite o nome do arquivo para download: ");
                            String arquivoParaDownload = scanner.nextLine();
                            client.requestDownload(ipDoPeer, Integer.parseInt(portaDoPeer), arquivoParaDownload);
                            //                    client.download(ipDoPeer, Integer.parseInt(portaDoPeer), arquivoParaDownload);
                            break;
                        default:
                            System.out.println("Opção inválida");
                            break;
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }

        }
    }


    public static void main(String[] args) {
        Client client = new Client();
        Scanner scanner = new Scanner(System.in);

        ClientThread clientThread = new ClientThread(client, scanner);
        clientThread.start();

    }

    private static class EscutaEDevolveemOutraThread extends Thread {
        private Path path;
        private Socket socket;

        public EscutaEDevolveemOutraThread(Socket socket, Path path) {
            this.path = path;
            this.socket = socket;
        }

        public void run() {
            try {
                DataInputStream inputServer = new DataInputStream(socket.getInputStream());
                String file = inputServer.readUTF();

                File sendFile = new File(path + "/" + file);

                OutputStream os = socket.getOutputStream();
                DataOutputStream writer = new DataOutputStream(os);

                writer.write(convertFileToByteArray(sendFile));
                socket.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private byte[] convertFileToByteArray(File file) throws IOException {
            try (FileInputStream fis = new FileInputStream(file);
                 ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[1000 * 1024];
                int bytesRead;

                while ((bytesRead = fis.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }

                return bos.toByteArray();
            }
        }
    }

    private static class ListenerThread extends Thread {
        private InetSocketAddress addrs;
        private Path path;

        public ListenerThread(InetSocketAddress addrs, Path path) {
            this.addrs = addrs;
            this.path = path;
        }

        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket();
                serverSocket.bind(addrs);
                while (true) {
                    Socket socket = serverSocket.accept(); //BLOCKING
                    new EscutaEDevolveemOutraThread(socket, path).start();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
