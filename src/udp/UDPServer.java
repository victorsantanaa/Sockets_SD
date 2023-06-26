package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {

    public static void main(String[] args) throws Exception {

        DatagramSocket serverSocket = new DatagramSocket(9876);

        while (true) {

            byte[] recBuffer = new byte[1024];

            DatagramPacket recPkt = new DatagramPacket(recBuffer, recBuffer.length);
            System.out.println("Esperando alguma mensagem");
            serverSocket.receive(recPkt);

            byte[] sendBuf = new byte[1024];
            sendBuf = "sou o servidor".getBytes();


            String informacao = new String(recPkt.getData(),
                    recPkt.getOffset(),
                    recPkt.getLength()
            );

            InetAddress IPAddress = recPkt.getAddress();
            int port = recPkt.getPort();

            DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, IPAddress, port);

            serverSocket.send(sendPacket);
            System.out.println("Mensagem enviada pelo Server");

            //serverSocket.close();
        }
    }
}
