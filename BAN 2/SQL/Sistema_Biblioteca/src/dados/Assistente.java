package dados;

public class Assistente {
    
    private int cod_a;
    private String nome;


    //Construtor para inserção:
    public Assistente(String nome) {
        this.nome = nome;
    }

    //Construtores
    public Assistente(int cod_a, String nome) {
        this.cod_a = cod_a;
        this.nome = nome;
    }

    //Getters e Setters
    public int getCod_a() {
        return cod_a;
    }
    
    public void setCod_a(int cod_a) {
        this.cod_a = cod_a;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Assistente { ");
        sb.append("cod_a: ").append(cod_a).append(", ");
        sb.append("nome: ").append(nome).append(" }");
        return sb.toString();
    }

    
   
    
    
    
}
