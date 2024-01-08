package persistencia;

import dados.Assistente;
import dados.Categoria;
import dados.Usuario;
import exceptions.SelectException;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UsuarioDAO {

    private static UsuarioDAO instance = null;

    private PreparedStatement insert;
    private PreparedStatement selectAll;
    private PreparedStatement selectNewId;
    private PreparedStatement Verifica_Se_Usuario_Existe;

    private UsuarioDAO() throws SQLException, ClassNotFoundException {
        Connection conexao = Conexao.getConexao();

        selectNewId = conexao.prepareStatement("select max(id)+1 from usuarios");

        insert = conexao.prepareStatement("insert into usuarios (id,nome,endereco,telefone,categoria,periodo) values (?,?,?,?,?,?);");

        Verifica_Se_Usuario_Existe = conexao.prepareStatement("select count(*) from usuarios where id = ?;");
        
        
        selectAll = conexao.prepareStatement("SELECT * from usuarios;");

    }
    
    public static UsuarioDAO getInstance() throws ClassNotFoundException, SQLException {
        if(instance == null) {
            instance = new UsuarioDAO();
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
            System.out.println("Erro ao buscar novo id na tabela usuarios");
        }
        return 0;
    }

    public void insert(Usuario Usuario_dados) {
        int dado_selectNewId = selectNewId();
        if(dado_selectNewId==0) {
            System.out.println("Erro ao buscar novo id para INSERIR USUARIO, cancelando operação");
            return;
        } 
        try {
        	 	insert.setInt(1,dado_selectNewId);
        	    insert.setString(2, Usuario_dados.getNome());
        	    insert.setString(3, Usuario_dados.getEndereco());
        	    insert.setString(4, Usuario_dados.getTelefone());
                insert.setString(5, Usuario_dados.getCategoria().name());
        	    insert.setString(6, Usuario_dados.getPeriodo());
        	    
        	    insert.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao inserir Usuario.");
        }
    }

    public Boolean Usuario_Existe(int cod_User) throws SelectException {
        try{
            Verifica_Se_Usuario_Existe.setInt(1, cod_User);
            ResultSet rs = Verifica_Se_Usuario_Existe.executeQuery();

            while(rs.next()) {
                int count = rs.getInt(1);
                if(count>0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao procurar se Usuario Existe ");
        }
        return false;
    }
    

    public List<Usuario> selectAll() throws SelectException {

        List<Usuario> lista_de_usuarios = new LinkedList<>();
        
        try{
            ResultSet rs = selectAll.executeQuery();

            while(rs.next()) {
                int id_usuario = rs.getInt(1);
                String nome = rs.getString(2);
                String endereco = rs.getString(3);
                String telefone = rs.getString(4);
                int id_assistente = rs.getInt(5);
                Date data_dev = rs.getDate(6);
                String categ = rs.getString(7);
                String periodo = rs.getString(8);
                
                
                Categoria categoria = null;
                Assistente assistente = new Assistente(id_assistente, null);
                try {
                    categoria = Usuario.obterCategoriaString(categ);
        
                } catch (IllegalArgumentException e) {
                    System.out.println("Erro ao capturar string de categoria na classe usuario");
                }
                Usuario usuario = new Usuario(id_usuario,nome,endereco,telefone,assistente,data_dev,periodo,categoria);
                    lista_de_usuarios.add(usuario);   
                
                         
                }           
            
        } catch (SQLException e) {
            throw new SelectException("Erro ao listar Usuarios ");
        }
        return lista_de_usuarios;
    }




}

