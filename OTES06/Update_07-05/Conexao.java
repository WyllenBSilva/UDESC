import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Conexao extends Thread {
    private static Map<String, PrintStream> clientes = new ConcurrentHashMap<>();
    private Socket cliente;
    private String nome;

    public Conexao(Socket cliente) {
        this.cliente = cliente;
    }

    public void run() {
        try {
            Scanner entrada = new Scanner(cliente.getInputStream());
            PrintStream saida = new PrintStream(cliente.getOutputStream());

            // Lê o nome do cliente
            this.nome = entrada.nextLine();
            clientes.put(nome, saida);

            // Log IP e horário
            logConexao(cliente.getInetAddress());

            while (entrada.hasNextLine()) {
                String linha = entrada.nextLine();

                if (linha.equals("/sair")) {
                    clientes.remove(nome);
                    cliente.close();
                    break;
                } else if (linha.equals("/users")) {
                    String usuarios = String.join(", ", clientes.keySet());
                    saida.println("Usuários conectados: " + usuarios);
                } else if (linha.startsWith("/send message")) {
                    // Processar mensagem para destinatário
                } else if (linha.startsWith("/send file")) {
                    // Processar envio de arquivo
                } else {
                    saida.println("Comando desconhecido.");
                }
            }

        } catch (IOException e) {
            System.out.println("Erro com cliente: " + nome);
        }
    }

    private void logConexao(InetAddress ip) {
        try (FileWriter fw = new FileWriter("log.txt", true)) {
            fw.write("Cliente conectado: " + ip + " em " + java.time.LocalDateTime.now() + "\n");
        } catch (IOException e) {
            System.out.println("Erro ao escrever log: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(7777);
        System.out.println("Servidor iniciado na porta 7777");

        while (true) {
            Socket socket = server.accept();
            new Conexao(socket).start();
        }
    }
}
