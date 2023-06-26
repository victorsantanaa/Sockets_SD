package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {

    public static void main(String[] args) throws Exception {

        InetAddress IPAddress = InetAddress.getByName("127.0.0.1");

        DatagramSocket clientSocket = new DatagramSocket();

        byte[] sendData = new byte[1024];
        sendData = "sou um cliente".getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);

        clientSocket.send(sendPacket);

        System.out.println("mensagem enviada para o servidor");

        byte[] recBuffer = new byte[1024];

        DatagramPacket recPkt = new DatagramPacket(recBuffer, recBuffer.length);

        clientSocket.receive(recPkt);

        String informacao = new String(recPkt.getData(),
                recPkt.getOffset(),
                recPkt.getLength()
        );

        System.out.println("recebido do servidor: " + informacao);

        clientSocket.close();
    }
}
