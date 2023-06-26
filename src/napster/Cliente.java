package napster;

import rmi.ServicoHora;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Cliente {

    public static void main(String[] args) throws Exception {
        System.out.println("Iniciando Cliente");
        Registry reg = LocateRegistry.getRegistry();
        ServerRemote serverRemote = (ServerRemote) reg.lookup("rmi://127.0.0.1/napster");

        String info = serverRemote.obterInfoServidor("Cliente X");
        System.out.println("Info: " + info);
        Socket s = new Socket("127.0.0.1", 9000);

        OutputStream os = s.getOutputStream();
        DataOutputStream writer = new DataOutputStream(os);

        InputStreamReader is = new InputStreamReader(s.getInputStream());
        BufferedReader reader = new BufferedReader(is);

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        String texto = inFromUser.readLine();

        writer.writeBytes(texto + "\n");

        String response = reader.readLine();
        System.out.println("DoServidor: " + response);

        s.close();
        System.out.println("Finalizou Cliente");
    }
}
