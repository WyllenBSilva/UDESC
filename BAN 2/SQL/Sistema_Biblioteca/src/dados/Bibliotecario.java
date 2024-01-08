package dados;

public class Bibliotecario {
    private int cod_b;
    private String nome;
    private Assistente assistente;


    //Construtor para insert:

    
    //Construtor
    public Bibliotecario(int cod_b, String nome, Assistente assistente) {
        this.cod_b = cod_b;
        this.nome = nome;
        this.assistente = assistente;
    }

    public Bibliotecario(String nome, Assistente assistente) {
        this.nome = nome;
        this.assistente = assistente;
    }

    // getters e setters
    public int getCod_b() {
        return cod_b;
    }

    public void setCod_b(int cod_b) {
        this.cod_b = cod_b;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Assistente getAssistente() {
        return assistente;
    }

    public void setAssistente(Assistente assistente) {
        this.assistente = assistente;
    }

    //@Override
    //public String toString() {
     //   return "Bibliotecario [cod_b=" + cod_b + ", nome=" + nome + ", assistente=" + assistente.getCod_a() + "]";
    //}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("| %5d | %-14s | %s |\n", cod_b, nome, assistente.getCod_a()));
        sb.append("------------------------------");
        return sb.toString();
    }
    

    

    
    

}
