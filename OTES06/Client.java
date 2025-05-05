import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {
        var client = new Socket("10.0.7.10", 7777);
        var teclado = new Scanner(System.in);
        var saida = new PrintStream(client.getOutputStream());

        // System.out.println("Qual seu nome?");
        // String nome = teclado.nextLine();
        // saida.println(nome);
    



        //aqui provavelmente vai ter que ter um código para encerrar a conexão caso o cliente saia.
        while(teclado.hasNextLine()) {
            
            saida.println(teclado.nextLine());
        

        }

        saida.close();
        teclado.close();
        client.close();



        
    }
}