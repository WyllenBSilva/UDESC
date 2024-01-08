package persistencia;

import dados.Assistente;
import exceptions.SelectException;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class AssistenteDAO {

    private static AssistenteDAO instance = null;

    private PreparedStatement insert;
    private PreparedStatement selectAll;
    private PreparedStatement selectNewId;
    private PreparedStatement Verifica_Se_Assistente_Existe;

    private AssistenteDAO() throws SQLException, ClassNotFoundException {
        Connection conexao = Conexao.getConexao();

        selectNewId = conexao.prepareStatement("select max(cod_a)+1 from assistente;");

        insert = conexao.prepareStatement("INSERT INTO assistente values(?,?);");

        Verifica_Se_Assistente_Existe = conexao.prepareStatement("select count(*) from assistente where cod_a = ?;");
        
        selectAll = conexao.prepareStatement("SELECT * from assistente;");

    }
    
    public static AssistenteDAO getInstance() throws ClassNotFoundException, SQLException {
        if(instance == null) {
            instance = new AssistenteDAO();
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
            System.out.println("Erro ao buscar novo id na tabela assistente");
        }
        return 0;
    }

    public void insert(Assistente Assistente_dados) {

        int dado_selectNewId = selectNewId();
        if(dado_selectNewId==0) {
            System.out.println("Erro ao buscar novo id para INSERIR Assistente, cancelando operação");
            return;
        } 

        try {
             insert.setInt(1,dado_selectNewId);
             insert.setString(2, Assistente_dados.getNome());
             insert.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao inserir Assistente.");
        }
    }

    public List<Assistente> selectAll() throws SelectException {

        List<Assistente> lista_de_assistentes = new LinkedList<>();
        
        try{
            ResultSet rs = selectAll.executeQuery();

            while(rs.next()) {
                int id_assistente = rs.getInt(1);
                String nome = rs.getString(2);

                lista_de_assistentes.add(new Assistente(id_assistente,nome));
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao listar Assistentes ");
        }
        return lista_de_assistentes;
    }

    public Boolean AssisteExiste(int cod_a) throws SelectException {
        try{
            Verifica_Se_Assistente_Existe.setInt(1, cod_a);
            ResultSet rs = Verifica_Se_Assistente_Existe.executeQuery();

            while(rs.next()) {
                int count = rs.getInt(1);
                if(count>0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao procurar se Assistente Existe ");
        }
        return false;
    }
    




}

