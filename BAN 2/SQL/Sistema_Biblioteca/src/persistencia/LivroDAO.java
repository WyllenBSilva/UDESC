package persistencia;

import dados.Livro;
import exceptions.SelectException;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class LivroDAO {

    private static LivroDAO instance = null;

    private PreparedStatement insert;
    private PreparedStatement selectAll;
    private PreparedStatement selectNewId;
    private PreparedStatement Verifica_Se_Livro_Existe;

    private LivroDAO() throws SQLException, ClassNotFoundException {
        Connection conexao = Conexao.getConexao();

        selectNewId = conexao.prepareStatement("select max(id)+1 from livros");

        insert = conexao.prepareStatement("select CadastraLivro(?,?,?,?,?);");
        //mudar o insert into para funcao de cadastro de livro. Mudar a funcao de cadastra livros e exemplares para duas.
        
        selectAll = conexao.prepareStatement("SELECT * from livros;");

        Verifica_Se_Livro_Existe = conexao.prepareStatement("select count(*) from livros where id = ?;");


    }
    
    public static LivroDAO getInstance() throws ClassNotFoundException, SQLException {
        if(instance == null) {
            instance = new LivroDAO();
        }
        return instance;
    }

    public int selectNewId() {
        try {
            ResultSet rs = selectNewId.executeQuery();
            if(rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar novo id na tabela livros");
        }
        return 0;
    }

    public void insert(Livro Livro_dados) {

        try {
             //insert.setInt(1,dado_selectNewId); nao é necessário para livro, a propria funcao de cadastro de livro faz isso.
             insert.setString(1, Livro_dados.getISBN());
             insert.setString(2, Livro_dados.getTitulo());
             insert.setString(3, Livro_dados.getAutor());
             insert.setString(4, Livro_dados.getEditora());
             insert.setString(5, Livro_dados.getColecao());
             insert.executeQuery();

        } catch (SQLException e) {
            System.out.println("Erro ao inserir Livro Na classe insert DAO");
        }
    }

    public List<Livro> selectAll() throws SelectException {

        List<Livro> lista_de_livros = new LinkedList<>();
        
        try{
            ResultSet rs = selectAll.executeQuery();

            while(rs.next()) {
                int id_livro = rs.getInt(1);
                String isbn = rs.getString(2);
                String titulo = rs.getString(3);
                String autor = rs.getString(4);
                String editora = rs.getString(5);
                String colecao = rs.getString(6);

                lista_de_livros.add(new Livro(id_livro,isbn,titulo,autor,editora,colecao));
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao listar Livros ");
        }
        return lista_de_livros;
    }

    public Boolean Livro_Existe(int cod_Livro) throws SelectException {
        try{
            Verifica_Se_Livro_Existe.setInt(1, cod_Livro);
            ResultSet rs = Verifica_Se_Livro_Existe.executeQuery();

            while(rs.next()) {
                int count = rs.getInt(1);
                if(count>0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao procurar se Livro Existe ");
        }
        return false;
    }
    




}

