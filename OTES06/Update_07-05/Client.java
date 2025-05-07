import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try {
            Socket client = new Socket("10.0.7.10", 7777); // IP e porta do servidor
            Scanner teclado = new Scanner(System.in);
            PrintStream saida = new PrintStream(client.getOutputStream());
            InputStream entradaBytes = client.getInputStream();
            Scanner entradaTexto = new Scanner(entradaBytes);

            System.out.println("Qual seu nome?");
            String nome = teclado.nextLine();
            saida.println(nome); // Envia nome ao servidor

            // Thread para receber mensagens e arquivos
            Thread recebimento = new Thread(() -> {
                try {
                    while (entradaTexto.hasNextLine()) {
                        String linha = entradaTexto.nextLine();

                        if (linha.startsWith("RECEBENDO_ARQUIVO")) {
                            // Exemplo: RECEBENDO_ARQUIVO arquivo.txt 2048
                            String[] partes = linha.split(" ");
                            String nomeArquivo = partes[1];
                            long tamanho = Long.parseLong(partes[2]);

                            System.out.println("Recebendo arquivo: " + nomeArquivo + " (" + tamanho + " bytes)");

                            try (FileOutputStream fos = new FileOutputStream(nomeArquivo)) {
                                byte[] buffer = new byte[4096];
                                long totalLido = 0;
                                while (totalLido < tamanho) {
                                    int bytesLidos = entradaBytes.read(buffer, 0, (int) Math.min(buffer.length, tamanho - totalLido));
                                    if (bytesLidos == -1) break;
                                    fos.write(buffer, 0, bytesLidos);
                                    totalLido += bytesLidos;
                                }
                                System.out.println("Arquivo '" + nomeArquivo + "' salvo com sucesso.");
                            } catch (IOException e) {
                                System.out.println("Erro ao salvar arquivo: " + e.getMessage());
                            }

                        } else {
                            // Mensagem normal
                            System.out.println(linha);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Conexão encerrada.");
                }
            });
            recebimento.start();

            // Loop principal: leitura de comandos do teclado
            while (true) {
                String linha = teclado.nextLine();

                if (linha.startsWith("/send message")) {
                    saida.println(linha);

                } else if (linha.startsWith("/send file")) {
                    String[] partes = linha.split(" ", 4);
                    if (partes.length < 4) {
                        System.out.println("Uso correto: /send file <destinatario> <caminho_arquivo>");
                        continue;
                    }
                    String destinatario = partes[2];
                    String caminhoArquivo = partes[3];
                    File arquivo = new File(caminhoArquivo);

                    if (!arquivo.exists()) {
                        System.out.println("Arquivo não encontrado.");
                        continue;
                    }

                    // Envia cabeçalho para o servidor
                    saida.println("/send file " + destinatario + " " + arquivo.getName() + " " + arquivo.length());

                    // Envia conteúdo do arquivo
                    try (BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());
                         FileInputStream fis = new FileInputStream(arquivo)) {

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            bos.write(buffer, 0, bytesRead);
                            bos.flush();
                        }

                        System.out.println("Arquivo enviado.");
                    } catch (IOException e) {
                        System.out.println("Erro ao enviar arquivo: " + e.getMessage());
                    }

                } else if (linha.equals("/users")) {
                    saida.println("/users");

                } else if (linha.equals("/sair")) {
                    saida.println("/sair");
                    break;

                } else {
                    System.out.println("Comando não reconhecido.");
                }
            }

            teclado.close();
            saida.close();
            entradaTexto.close();
            client.close();
            System.out.println("Desconectado.");

        } catch (Exception e) {
            System.out.println("Erro no cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
