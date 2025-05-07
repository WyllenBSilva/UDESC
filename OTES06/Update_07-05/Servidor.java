import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Servidor {

    // Armazena clientes conectados (nome -> PrintStream para enviar mensagens)
    private static Map<String, ClienteHandler> clientes = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        int porta = 7777;
        ServerSocket servidor = new ServerSocket(porta);
        System.out.println("Servidor iniciado na porta " + porta);

        while (true) {
            Socket clienteSocket = servidor.accept();
            new ClienteHandler(clienteSocket).start();
        }
    }

    // Classe que lida com cada cliente em uma thread separada
    private static class ClienteHandler extends Thread {
        private Socket socket;
        private PrintStream out;
        private Scanner in;
        private String nome;

        public ClienteHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.in = new Scanner(socket.getInputStream());
            this.out = new PrintStream(socket.getOutputStream());
        }

        @Override
        public void run() {
            try {
                // Recebe o nome do cliente e registra
                nome = in.nextLine();
                clientes.put(nome, this);

                // Log de conexão
                logConexao(socket.getInetAddress());

                System.out.println("Cliente '" + nome + "' conectado.");

                while (in.hasNextLine()) {
                    String linha = in.nextLine();

                    if (linha.equals("/sair")) {
                        clientes.remove(nome);
                        socket.close();
                        System.out.println("Cliente '" + nome + "' desconectado.");
                        break;
                    } else if (linha.equals("/users")) {
                        out.println("Usuários conectados: " + String.join(", ", clientes.keySet()));
                    } else if (linha.startsWith("/send message")) {
                        String[] partes = linha.split(" ", 4);
                        if (partes.length < 4) {
                            out.println("Uso: /send message <destinatario> <mensagem>");
                            continue;
                        }
                        String destinatario = partes[2];
                        String mensagem = partes[3];
                        ClienteHandler destino = clientes.get(destinatario);
                        if (destino != null) {
                            destino.out.println(nome + ": " + mensagem);
                        } else {
                            out.println("Usuário '" + destinatario + "' não encontrado.");
                        }
                    } else if (linha.startsWith("/send file")) {
                        // Ex: /send file Bob nome.txt 1024
                        String[] partes = linha.split(" ", 5);
                        if (partes.length < 5) {
                            out.println("Uso: /send file <destinatario> <nome_arquivo> <tamanho_bytes>");
                            continue;
                        }

                        String destinatario = partes[2];
                        String nomeArquivo = partes[3];
                        long tamanho = Long.parseLong(partes[4]);

                        ClienteHandler destino = clientes.get(destinatario);
                        if (destino == null) {
                            out.println("Usuário '" + destinatario + "' não encontrado.");
                            continue;
                        }

                        // Confirma que o destinatário existe, agora transfere os bytes
                        out.println("Enviando arquivo para " + destinatario + "...");

                        OutputStream destinoOut = destino.socket.getOutputStream();
                        InputStream origemIn = socket.getInputStream();

                        destino.out.println("RECEBENDO_ARQUIVO " + nomeArquivo + " " + tamanho);

                        byte[] buffer = new byte[4096];
                        long totalLido = 0;

                        while (totalLido < tamanho) {
                            int bytesLidos = origemIn.read(buffer, 0, (int)Math.min(buffer.length, tamanho - totalLido));
                            if (bytesLidos == -1) break;
                            destinoOut.write(buffer, 0, bytesLidos);
                            totalLido += bytesLidos;
                        }

                        destinoOut.flush();
                        out.println("Arquivo enviado com sucesso.");
                        destino.out.println("Arquivo '" + nomeArquivo + "' recebido de " + nome);

                    } else {
                        out.println("Comando não reconhecido.");
                    }
                }

            } catch (Exception e) {
                System.out.println("Erro com cliente '" + nome + "': " + e.getMessage());
            } finally {
                try {
                    clientes.remove(nome);
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Erro ao fechar conexão de '" + nome + "'");
                }
            }
        }

        private void logConexao(InetAddress ip) {
            try (FileWriter fw = new FileWriter("log.txt", true)) {
                fw.write("Cliente: " + nome + " | IP: " + ip.getHostAddress() + " | Data: " + LocalDateTime.now() + "\n");
            } catch (IOException e) {
                System.out.println("Erro ao registrar log.");
            }
        }
    }
}
