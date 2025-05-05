import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Conexao extends Thread {
    private Socket cliente;

    public Conexao(final Socket cliente) {
        this.cliente = cliente;
    }

    // ArrayList<String> users = new ArrayList<>();

    // public void AddUser(String nome) {
    //     users.add(nome);
    // }   


    // public void Lista_Todos_clientes() {
    //     for (String name : users)  {  
    //         System.out.println(name);  
    //     }  
    // }


    

    public void run() {
        Scanner saida = null;
        InetAddress ip = null;
        try {
            saida = new Scanner(cliente.getInputStream());
            ip = cliente.getInetAddress();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        while (saida.hasNextLine()) {

            String mensagem = saida.nextLine();

            // String nome = mensagem;
            // AddUser(nome);
            // System.out.println("Cliente adicionado: " + nome );

            // Lista_Todos_clientes();

            if(mensagem.equals("/sair")) {
                System.out.println("Encerrando conexão do cliente com IP: " + ip );
                try {
                    cliente.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }

            System.out.println(mensagem);

            
        }


        
        saida.close();
    }



    public static void main(String[] args) throws IOException{

        var port = 7777;
        try (var server = new ServerSocket(port)) {
            System.out.println("Server Launched on port: " + port);

            while(true) { //esse true provavelmente será substituído por "/sair" ?
                var socket = server.accept();
                System.out.println("Connection established!");
                new Conexao(socket).start();
            }
        }
        
    }
}
