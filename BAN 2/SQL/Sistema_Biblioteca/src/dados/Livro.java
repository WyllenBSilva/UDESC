package dados;

public class Livro {
    private int id;
    private String ISBN;
    private String titulo;
    private String autor;
    private String editora;
    private String colecao;

    //Construtor
    public Livro(int id, String iSBN, String titulo, String autor, String editora, String colecao) {
        this.id = id;
        ISBN = iSBN;
        this.titulo = titulo;
        this.autor = autor;
        this.editora = editora;
        this.colecao = colecao;
    }

    //Construtor para inserção de um novo livro.
    public Livro(String iSBN, String titulo, String autor, String editora, String colecao) {
        ISBN = iSBN;
        this.titulo = titulo;
        this.autor = autor;
        this.editora = editora;
        this.colecao = colecao;
    }

    //Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getISBN() {
        return ISBN;
    }
    public void setISBN(String iSBN) {
        ISBN = iSBN;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }
    public String getEditora() {
        return editora;
    }
    public void setEditora(String editora) {
        this.editora = editora;
    }
    public String getColecao() {
        return colecao;
    }
    public void setColecao(String colecao) {
        this.colecao = colecao;
    }

    @Override
    public String toString() {

        if(ISBN==null) {
            return 
            " Título: " + titulo +
            ", Autor: " + autor
            ;
        } else {
            return " ID: " + id +
               ", ISBN: " + ISBN +
               "\n Título: " + titulo +
               ", Autor: " + autor +
               "\n Editora: " + editora +
               ", Coleção: " + colecao +
               "\n";

        }
        
    }

    

    
}
