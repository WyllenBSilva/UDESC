package persistencia;

//import dados.Emprestimo;
import dados.Exemplar;
//import dados.Livro;
import dados.Reserva;
import dados.Usuario;
import exceptions.SelectException;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;



public class ReservaDAO {

    private static ReservaDAO instance = null;

    private PreparedStatement insert;
    private PreparedStatement selectAll;
    private PreparedStatement selectNewId;
    

    private ReservaDAO() throws SQLException, ClassNotFoundException {
        Connection conexao = Conexao.getConexao();

        selectNewId = conexao.prepareStatement("select max(id)+1 from reservas");

        insert = conexao.prepareStatement("select Reserva_Livro(?,?);");
        
        
        selectAll = conexao.prepareStatement("SELECT * from reservas;");
        

    }
    
    public static ReservaDAO getInstance() throws ClassNotFoundException, SQLException {
        if(instance == null) {
            instance = new ReservaDAO();
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
            System.out.println("Erro ao buscar novo id na tabela reservas");
        }
        return 0;
    }

    
    public boolean estaEmprestado(int id_exemplar) {
    	EmprestimoDAO emprestimoDao = null;
    	int id_user = -1;
    	try {
			id_user = emprestimoDao.selectOne(id_exemplar);
		} catch (SelectException e) {
			System.out.println("Erro ao procurar Usuario do emprestimo");
		}
    	if(id_user == -1) {
    		return false;
    	}
		return true;
    }
    
    public int insert(Reserva reserva_dados) {
        try {
             insert.setInt(1, reserva_dados.getUsuario().getId());
             insert.setInt(2, reserva_dados.getExemplar().getId());
             insert.executeQuery();
            return 1;

        } catch (SQLException e) {
            String errorMessage = e.getMessage();
            int endIndex = errorMessage.indexOf("\n");
            String error = (endIndex != -1) ? errorMessage.substring(0, endIndex) : errorMessage;

            if (error != null) {
                System.out.println("Erro SQL: " + error);
            }
            return 0;
            
            
        } 
        
    }

    public List<Reserva> selectAll() throws SelectException {

        List<Reserva> lista_de_reservas = new LinkedList<>();
        
        try{
            ResultSet rs = selectAll.executeQuery();

            while(rs.next()) {
                int id_reserva = rs.getInt(1);
                int id_user = rs.getInt(2);
                int id_exemplar = rs.getInt(3);
                Date data = rs.getDate(4);
                String hora = rs.getString(5);
                
                
                Usuario user = new Usuario(id_user, null, null, null, null, null, null, null);
                Exemplar exem = new Exemplar(id_exemplar, null, null, null);
                
                
                
                List<Usuario> lista_users = new LinkedList<Usuario>();
                UsuarioDAO usuarioDao = null;
                
                lista_users = usuarioDao.selectAll();
                
                for (int i = 0; i < lista_users.size(); i++) {
                	if (lista_users.get(i).getId() == id_user) {
                		user.setAssistente(lista_users.get(i).getAssistente());
                		user.setCategoria(lista_users.get(i).getCategoria());
                		user.setData_dev(lista_users.get(i).getData_dev());
                		user.setEndereco(lista_users.get(i).getEndereco());
                		user.setNome(lista_users.get(i).getNome());
                		user.setPeriodo(lista_users.get(i).getPeriodo());
                		user.setTelefone(lista_users.get(i).getTelefone());
                		break;
                	}
                }
                
                
                List<Exemplar> lista_exemplares = new LinkedList<Exemplar>();
                ExemplarDAO exemplarDao = null;
                
                lista_exemplares = exemplarDao.selectAll();
                
                for (int i = 0; i < lista_exemplares.size(); i++) {
                	if (lista_exemplares.get(i).getId() == id_exemplar) {
                		exem.setAssistente(lista_exemplares.get(i).getAssistente());
                		exem.setLivro(lista_exemplares.get(i).getLivro());
                		exem.setLocalizacao(lista_exemplares.get(i).getLocalizacao());
                		break;
                	}
                }
                
                
                
                lista_de_reservas.add(new Reserva(id_reserva,user,exem,data,hora));
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao listar Reservas");
        }
        return lista_de_reservas;
    }


    

}

