import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Servidor {

    private static Map<String, ClienteHandler> clientes = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        int porta = 7777;
        try (ServerSocket servidor = new ServerSocket(porta)) {
            System.out.println("Servidor iniciado na porta " + porta);
            while (true) {
                Socket clienteSocket = servidor.accept();
                new ClienteHandler(clienteSocket).start();
            }
        }
    }

    private static class ClienteHandler extends Thread {
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;
        private String nome;

        public ClienteHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        }

        @Override
        public void run() {
            try {
                nome = in.readUTF();
                clientes.put(nome, this);
                logConexao(socket.getInetAddress());

                System.out.println("Cliente '" + nome + "' conectado.");

                while (true) {
                    String linha = in.readUTF();
                    String[] partes = linha.trim().split("\\s+", 4);

                    switch (partes[0]) {
                        case "/sair":
                            clientes.remove(nome);
                            socket.close();
                            System.out.println("Cliente '" + nome + "' desconectado.");
                            return;

                        case "/users":
                            out.writeUTF("Usuários conectados: " + String.join(", ", clientes.keySet()));
                            break;

                        case "/send":
                            if (partes.length < 4) break;

                            if (partes[1].equals("message")) {
                                String destino = partes[2];
                                String msg = partes[3];
                                ClienteHandler receptor = clientes.get(destino);
                                if (receptor != null) {
                                    receptor.out.writeUTF(nome + ": " + msg);
                                } else {
                                    out.writeUTF("Usuário '" + destino + "' não encontrado.");
                                }
                            }

                            else if (partes[1].equals("file")) {
                                String destino = partes[2];
                                String nomeArquivo = partes[3];

                                ClienteHandler receptor = clientes.get(destino);
                                if (receptor == null) {
                                    out.writeUTF("Usuário '" + destino + "' não encontrado.");
                                    continue;
                                }

                                long tamanho = in.readLong();
                                receptor.out.writeUTF("RECEBENDO_ARQUIVO " + nomeArquivo);
                                receptor.out.writeLong(tamanho);

                                byte[] buffer = new byte[4096];
                                long totalLido = 0;
                                int bytesLidos;
                                while (totalLido < tamanho && (bytesLidos = in.read(buffer)) != -1) {
                                    receptor.out.write(buffer, 0, bytesLidos);
                                    totalLido += bytesLidos;
                                }

                                out.writeUTF("Arquivo enviado com sucesso.");
                                receptor.out.writeUTF("Arquivo '" + nomeArquivo + "' recebido de " + nome);
                            }
                            break;

                        default:
                            out.writeUTF("Comando não reconhecido.");
                    }
                }

            } catch (Exception e) {
                System.out.println("Erro com cliente '" + nome + "': " + e.getMessage());
            } finally {
                try {
                    clientes.remove(nome);
                    socket.close();
                } catch (IOException ignored) {}
            }
        }

        private void logConexao(InetAddress ip) {
            String arquivo = "log.csv";
            Path caminho = Paths.get(arquivo);
            boolean adicionarCabecalho = !Files.exists(caminho) || isArquivoVazio(caminho);

            LocalDateTime agora = LocalDateTime.now();
            String data = agora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String hora = agora.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            try (FileWriter fw = new FileWriter(arquivo, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {

                if (adicionarCabecalho) {
                    bw.write("Cliente, Endereco_IP, Data_Conexao, Hora_Conexao\n");
                }

                bw.write(nome + ", " + ip.getHostAddress() + ", " + data + ", " + hora + "\n");

            } catch (IOException e) {
                System.out.println("Erro ao registrar log.");
            }
        }

        private boolean isArquivoVazio(Path caminho) {
            try {
                return Files.size(caminho) == 0;
            } catch (IOException e) {
                return true;
            }
        }
    }
}
