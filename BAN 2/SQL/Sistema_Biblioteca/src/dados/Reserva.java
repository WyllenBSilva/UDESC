package dados;

import java.sql.Date;

public class Reserva {
    private int id;
    private Usuario usuario;
    private Exemplar exemplar;
    private Date data;
    private String hora;

    //Construtor
    public Reserva(int id, Usuario usuario, Exemplar exemplar, Date data, String hora) {
        this.id = id;
        this.usuario = usuario;
        this.exemplar = exemplar;
        this.data = data;
        this.hora = hora;
    }

    
    public Reserva(Usuario usuario, Exemplar exemplar) {
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
    public String getHora() {
        return hora;
    }
    public void setHora(String hora) {
        this.hora = hora;
    }
    @Override
    public String toString() {
        return "Reserva [id=" + id + ", usuario=" + usuario + ", exemplar=" + exemplar + ", data=" + data + ", hora="
                + hora + "]";
    }


    
    



}
