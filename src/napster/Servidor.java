package napster;

import java.io.*;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Servidor {



    public static void main(String[] args) throws Exception {

        ServerRemote serverRemote = new ServerRemoteImpl();
        LocateRegistry.createRegistry(1099);

        Registry reg = LocateRegistry.getRegistry();
        reg.bind("rmi://127.0.0.1/napster", serverRemote);
        System.out.println("-----Servidor no Ar-----");

        ServerSocket serverSocket = new ServerSocket(9000);

        while (true) {
            System.out.println("Esperando conexão do Socket");
            Socket no = serverSocket.accept();
            System.out.println("Conexão aceita do Socket");

            ThreadServidor threadServidor = new ThreadServidor(no);
            threadServidor.start();

        }
    }

    static class ThreadServidor extends Thread {
        private Socket no;

        public ThreadServidor(Socket node) {no = node;}

        @Override
        public void run() {
            try {
                System.out.println("Método: ThreadServidor.run() / Inciando Run");

                InputStreamReader is = new InputStreamReader(no.getInputStream());
                System.out.println("Método: ThreadServidor.run() / InputStreamReader inicializado");
                BufferedReader reader = new BufferedReader(is);
                System.out.println("Método: ThreadServidor.run() / Inicializado BufferedReader");

                OutputStream os = no.getOutputStream();
                System.out.println("Método: ThreadServidor.run() / getOutputStream do no passado no construtor de ThreadServidor");
                DataOutputStream writer = new DataOutputStream(os);
                System.out.println("Método: ThreadServidor.run() / Criação do Writer");

                String texto = reader.readLine();
                System.out.println("Método: ThreadServidor.run() / Lê a linha e coloca em uma variavel texto");
                System.out.println("Método: ThreadServidor.run() / " + texto);
                writer.writeBytes(texto.toUpperCase() + "\n");
                System.out.println("Método: ThreadServidor.run() / writeBytes: ");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
