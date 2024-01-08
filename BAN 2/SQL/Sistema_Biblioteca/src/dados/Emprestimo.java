package dados;

import java.sql.Date;

public class Emprestimo {
    private int id;
    private Usuario usuario;
    private Exemplar exemplar;
    private Date data;
    private float multas;
    private Date data_dev;

    //Construtor
    public Emprestimo(int id, Usuario usuario, Exemplar exemplar, Date data, float multas, Date data_dev) {
        this.id = id;
        this.usuario = usuario;
        this.exemplar = exemplar;
        this.data = data;
        this.multas = multas;
        this.data_dev = data_dev;
    }
    


    public Emprestimo(int id, Usuario usuario, Exemplar exemplar) {
		this.id = id;
		this.usuario = usuario;
		this.exemplar = exemplar;
	}



	public Emprestimo(Usuario usuario, Exemplar exemplar) {
        this.usuario = usuario;
        this.exemplar = exemplar;
    }



    //Getters e Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public Exemplar getExemplar() {
        return exemplar;
    }
    public void setExemplar(Exemplar exemplar) {
        this.exemplar = exemplar;
    }
    public Date getData() {
        return data;
    }
    public void setData(Date data) {
        this.data = data;
    }
    public float getMultas() {
        return multas;
    }
    public void setMultas(float multas) {
        this.multas = multas;
    }
    public Date getData_dev() {
        return data_dev;
    }
    public void setData_dev(Date data_dev) {
        this.data_dev = data_dev;
    }

    
    @Override
    public String toString() {
        if(usuario.getEndereco() == null) {
            return "Emprestimo [id=" + id + ", Nome_usuario: " + usuario.getNome() + ", Titulo_Livro: " + exemplar.getLivro().getTitulo();
        } else {
            return "Emprestimo [id=" + id + ", usuario=" + usuario + ", exemplar=" + exemplar + ", data=" + data
                + ", multas=" + multas + ", data_dev=" + data_dev + "]";
        }
        
    }

    
    
}
