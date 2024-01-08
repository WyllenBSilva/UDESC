package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import exceptions.*;

import dados.Assistente;
import dados.Bibliotecario;

public class BibliotecarioDAO {
    
    private static BibliotecarioDAO instance = null;

    private PreparedStatement insert;
    private PreparedStatement selectNewId;
    private PreparedStatement selectAll;

    private BibliotecarioDAO() throws SQLException, ClassNotFoundException {

        Connection conexao = Conexao.getConexao();

        selectNewId = conexao.prepareStatement("select max(cod_b)+1 from bibliotecario");

        insert = conexao.prepareStatement("INSERT INTO bibliotecario values(?,?,?);");

        selectAll = conexao.prepareStatement("select * from bibliotecario;");

    }
    
    public static BibliotecarioDAO getInstance() throws ClassNotFoundException, SQLException {
        if(instance == null) {
            instance = new BibliotecarioDAO();
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
            System.out.println("Erro ao buscar novo id na tabela Bibliotecario");
        }
        return 0;
    }

    public void insert(Bibliotecario Bibliotecario_dados) {
        int dado_selectNewId = selectNewId();
        if(dado_selectNewId==0) {
            System.out.println("Erro ao buscar novo id para INSERIR bibliotecario, cancelando operação");
            return;
        } 
        try {
             insert.setInt(1,dado_selectNewId);
             insert.setString(2, Bibliotecario_dados.getNome());
             insert.setInt(3, Bibliotecario_dados.getAssistente().getCod_a());
             insert.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao inserir Bibliotecario.");
        }
    }

    public List<Bibliotecario> selectAll() throws SelectException {

        List<Bibliotecario> lista_de_bibliotecario = new LinkedList<>();
        
        try{
            ResultSet rs = selectAll.executeQuery();

            while(rs.next()) {
                int cod_b = rs.getInt(1);
                String nome = rs.getString(2);
                int cod_a = rs.getInt(3);

                Assistente assistente_dado = new Assistente(cod_a, "Aux");
                lista_de_bibliotecario.add(new Bibliotecario(cod_b,nome,assistente_dado));
                assistente_dado = null; //remove o objeto do tipo assistente
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao listar Bibliotecarios ");
        }
        return lista_de_bibliotecario;
    }

}
