package dados;

public class Exemplar {
    private int id;
    private Livro livro;
    private Assistente assistente;
    private String localizacao;

    //Construtor
    public Exemplar(int id, Livro livro, Assistente assistente, String localizacao) {
        this.id = id;
        this.livro = livro;
        this.assistente = assistente;
        this.localizacao = localizacao;
    }
    
   
    public Exemplar(int id) {
        this.id = id;
    }


    //para insercao
    public Exemplar(Livro livro, String localizacao) {
        this.livro = livro;
        this.localizacao = localizacao;
    }



    //Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Assistente getAssistente() {
        return assistente;
    }

    public void setAssistente(Assistente assistente) {
        this.assistente = assistente;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    @Override
    public String toString() {

        if(livro.getTitulo() == null) {
            return "Exemplar [id=" + id + ", Id_livro=" + livro.getId() + ", assistente=" + assistente.getCod_a() + ", localizacao=" + localizacao
            + "]";
        } else {
            return "Exemplar [id=" + id + ", livro=" + livro + ", assistente=" + assistente + ", localizacao=" + localizacao
                + "]";
        }
        
    }  


    
    

    
}
