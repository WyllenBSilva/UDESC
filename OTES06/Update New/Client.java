import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String IP_local = localHost.getHostAddress();

            Socket client = new Socket(IP_local, 7777);
            Scanner teclado = new Scanner(System.in);

            // Fluxos separados
            DataOutputStream dataOut = new DataOutputStream(client.getOutputStream());
            DataInputStream dataIn = new DataInputStream(client.getInputStream());
            Scanner entradaTexto = new Scanner(dataIn);

            System.out.println("Qual seu nome?");
            String nome = teclado.nextLine();
            dataOut.writeUTF(nome);

            // Thread de recebimento
            Thread recebimento = new Thread(() -> {
                try {
                    while (true) {
                        String linha = dataIn.readUTF();

                        if (linha.startsWith("RECEBENDO_ARQUIVO")) {
                            String[] partes = linha.trim().split("\\s+");
                            String nomeArquivo = partes[1];
                            long tamanho = dataIn.readLong();

                            try (FileOutputStream fos = new FileOutputStream(nomeArquivo)) {
                                byte[] buffer = new byte[4096];
                                long totalLido = 0;
                                int bytesLidos;
                                while (totalLido < tamanho && (bytesLidos = dataIn.read(buffer)) != -1) {
                                    fos.write(buffer, 0, bytesLidos);
                                    totalLido += bytesLidos;
                                }
                                System.out.println("Arquivo '" + nomeArquivo + "' salvo com sucesso.");
                            }
                        } else {
                            System.out.println(linha);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Conexão encerrada.");
                }
            });
            recebimento.start();

            // Loop de envio de comandos
            while (true) {
                String linha = teclado.nextLine();
                String[] partes = linha.trim().split("\\s+", 4);

                switch (partes[0]) {
                    case "/send":
                        if (partes.length < 4) {
                            System.out.println("Use: /send [message/file] <destinatario> <mensagem>");
                            break;
                        }
                        if (partes[1].equals("message")) {
                            dataOut.writeUTF(linha);
                        } else if (partes[1].equals("file")) {
                            String destinatario = partes[2];
                            String caminhoArquivo = partes[3];
                            File arquivo = new File(caminhoArquivo);

                            if (!arquivo.exists()) {
                                System.out.println("Arquivo não encontrado.");
                                break;
                            }

                            dataOut.writeUTF("/send file " + destinatario + " " + arquivo.getName());
                            dataOut.writeLong(arquivo.length());

                            try (FileInputStream fis = new FileInputStream(arquivo)) {
                                byte[] buffer = new byte[4096];
                                int bytesRead;
                                while ((bytesRead = fis.read(buffer)) != -1) {
                                    dataOut.write(buffer, 0, bytesRead);
                                }
                                System.out.println("Arquivo enviado.");
                            }
                        }
                        break;

                    case "/users":
                    case "/sair":
                        dataOut.writeUTF(linha);
                        if (partes[0].equals("/sair")) return;
                        break;

                    default:
                        System.out.println("Comando não reconhecido.");
                }
            }

        } catch (Exception e) {
            System.out.println("Erro no cliente: " + e.getMessage());
        }
    }
}
