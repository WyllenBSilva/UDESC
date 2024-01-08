package persistencia;

import dados.Assistente;
import dados.Exemplar;
import dados.Livro;
import exceptions.SelectException;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ExemplarDAO {

    private static ExemplarDAO instance = null;

    private PreparedStatement insert;
    private PreparedStatement selectAll;
    private PreparedStatement selectNewId;
    private PreparedStatement Nome_Livro_Exemplar;
    private PreparedStatement Verifica_Se_Exemplar_Existe;

    private ExemplarDAO() throws SQLException, ClassNotFoundException {
        Connection conexao = Conexao.getConexao();

        selectNewId = conexao.prepareStatement("select max(id)+1 from exemplares");

        insert = conexao.prepareStatement("INSERT INTO exemplares (id,id_livro,localizacao) values(?,?,?);");
        
        
        selectAll = conexao.prepareStatement("SELECT * from exemplares;");

        Nome_Livro_Exemplar = conexao.prepareStatement("select ex.id as id_exemplar,l.titulo,l.autor from exemplares ex join livros l on ex.id_livro = l.id;");

        Verifica_Se_Exemplar_Existe = conexao.prepareStatement("select count(*) from exemplares where id = ?;");

    }
    
    public static ExemplarDAO getInstance() throws ClassNotFoundException, SQLException {
        if(instance == null) {
            instance = new ExemplarDAO();
        }
        return instance;
    }

    public List<Exemplar> Nome_Livro_Exemplar() throws SelectException {
        List<Exemplar> lista_de_Exemplares = new LinkedList<>();
        
        try{
            ResultSet rs = Nome_Livro_Exemplar.executeQuery();

            while(rs.next()) {
                int id_exemplar = rs.getInt(1);
                String livro_titulo = rs.getString(2);
                String livro_autor = rs.getString(3);
                Livro livro = new Livro(null, livro_titulo, livro_autor, null, null);

                lista_de_Exemplares.add(new Exemplar(id_exemplar,livro,null,null));
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao listar Exemplares ");
        }
        return lista_de_Exemplares;
    }

    public int selectNewId() {
        try {
            ResultSet rs = selectNewId.executeQuery();
            if(rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar novo id na tabela exemplares");
        }
        return 0;
    }

    public void insert(Exemplar Exemplar_dados) {
        try {
        	 insert.setInt(1,selectNewId());
             insert.setInt(2, Exemplar_dados.getLivro().getId());
             insert.setString(3, Exemplar_dados.getLocalizacao());
             insert.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao inserir Exemplar.");
        }
    }


    public List<Exemplar> selectAll() throws SelectException {

        List<Exemplar> lista_de_Exemplares = new LinkedList<>();
        
        try{
            ResultSet rs = selectAll.executeQuery();

            while(rs.next()) {
                int id_exemplar = rs.getInt(1);
                int id_livro = rs.getInt(2);
                int id_assistente = rs.getInt(3);
                String localizacao = rs.getString(4);
                Assistente assistente = new Assistente(id_assistente, "Nome aux");
                Livro livro = new Livro(id_livro, null, null, null, null, null);

                lista_de_Exemplares.add(new Exemplar(id_exemplar,livro,assistente,localizacao));
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao listar Exemplares ");
        }
        return lista_de_Exemplares;
    }

    public Boolean Exemplar_Existe(int cod_Exemplar) throws SelectException {
        try{
            Verifica_Se_Exemplar_Existe.setInt(1, cod_Exemplar);
            ResultSet rs = Verifica_Se_Exemplar_Existe.executeQuery();

            while(rs.next()) {
                int count = rs.getInt(1);
                if(count>0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao procurar se Exemplar Existe ");
        }
        return false;
    }
    



}

