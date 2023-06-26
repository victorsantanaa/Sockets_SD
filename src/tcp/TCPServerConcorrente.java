package tcp;

import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerConcorrente {

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(9000);

        while (true) {

            System.out.println("Esperando conexão");
            Socket no = serverSocket.accept();
            System.out.println("Conexão aceita");

            ThreadAtendimento thread = new ThreadAtendimento(no);
            thread.start();
        }

    }
}
