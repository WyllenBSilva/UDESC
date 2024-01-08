package dados;

import java.sql.Date;

public class Usuario {
    private int id;
    private String nome;
    private String endereco;
    private String telefone;
    private Assistente assistente;
    private Date data_dev;
    private String periodo;
    private Categoria categoria;

    
    

    public Usuario(int id) {
        this.id = id;
    }

    //Construtor
    public Usuario(int id, String nome, String endereco, String telefone, Assistente assistente, Date data_dev,String periodo, Categoria categoria) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.assistente = assistente;
        this.data_dev = data_dev;
        this.periodo = periodo;
        this.categoria = categoria;
    }
    
    //Construtor para inserção de um novo usuário:
    public Usuario(String nome, String endereco, String telefone, String periodo, Categoria categoria) {
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.periodo = periodo;
        this.categoria = categoria;
    }

    public static Categoria obterCategoria(int valor) {
        switch (valor) {
            case 1:
                return Categoria.ALUNO_GRADUACAO;
            case 2:
                return Categoria.ALUNO_POS_GRADUACAO;
            case 3:
                return Categoria.PROFESSOR;
            case 4:
                return Categoria.PROFESSOR_POS_GRADUACAO;
            default:
                throw new IllegalArgumentException("Valor inválido para categoria");
        }
    }

    public static Categoria obterCategoriaString(String valor) {
        switch (valor) {
            case "ALUNO_GRADUACAO":
                return Categoria.ALUNO_GRADUACAO;
            case "ALUNO_POS_GRADUACAO":
                return Categoria.ALUNO_POS_GRADUACAO;
            case "PROFESSOR":
                return Categoria.PROFESSOR;
            case "PROFESSOR_POS_GRADUACAO":
                return Categoria.PROFESSOR_POS_GRADUACAO;
            default:
                throw new IllegalArgumentException("Valor inválido para categoria");
        }
    }



    //Getters e Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public Assistente getAssistente() {
        return assistente;
    }
    public void setAssistente(Assistente assistente) {
        this.assistente = assistente;
    }
    public Date getData_dev() {
        return data_dev;
    }
    public void setData_dev(Date data_dev) {
        this.data_dev = data_dev;
    }
    public String getPeriodo() {
        return periodo;
    }
    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
    public Categoria getCategoria() {
        return categoria;
    }
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }


    @Override
    public String toString() {

        if(assistente.getNome()==null) {
            return "Usuario [id=" + id + ", nome=" + nome + ", endereco=" + endereco + ", telefone=" + telefone + ", periodo=" + periodo + ", categoria=" + categoria + "]";
        
        } else {
            return "Usuario [id=" + id + ", nome=" + nome + ", endereco=" + endereco + ", telefone=" + telefone
                + ", assistente=" + assistente + ", data_dev=" + data_dev + ", periodo=" + periodo + ", categoria="
                + categoria + "]";

        }
        
    }
    
    
    
    

    
    


}
