package persistencia;

import dados.Emprestimo;
import dados.Exemplar;
import dados.Livro;
import dados.Usuario;
import exceptions.SelectException;


import java.sql.Connection;
//import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


public class EmprestimoDAO {

    private static EmprestimoDAO instance = null;

    private PreparedStatement insert;
    private PreparedStatement selectAll;
    private PreparedStatement findUser;
    private PreparedStatement selectNewId;
    private PreparedStatement Verifica_Se_Exemplar_esta_Emprestado;
    private PreparedStatement Verifica_Se_Exemplar_Existe_Tabela_Emprestimo;
    private PreparedStatement localiza_usuario_exemplar;
    private PreparedStatement Usuario_com_exemplar;
    
    

    private EmprestimoDAO() throws SQLException, ClassNotFoundException {
        Connection conexao = Conexao.getConexao();

        selectNewId = conexao.prepareStatement("select max(id)+1 from emprestimo");

        insert = conexao.prepareStatement("select Empresta_Livro(?,?)");
        
        
        selectAll = conexao.prepareStatement("select e.id,u.nome, l.titulo from emprestimo e join usuarios u on e.id_user = u.id join exemplares ex on e.id_exemplar = ex.id join livros l on ex.id_livro = l.id");
        
        findUser = conexao.prepareStatement("SELECT id_user from emprestimo where id_exemplar = ?");

        Verifica_Se_Exemplar_esta_Emprestado = conexao.prepareStatement("select count(*) from emprestimo where id_exemplar = ? and multas is not null;");

        Verifica_Se_Exemplar_Existe_Tabela_Emprestimo = conexao.prepareStatement("select count(*) from emprestimo where id_exemplar = ?;");

        localiza_usuario_exemplar = conexao.prepareStatement("select procura_Usuario_Exemplar(?)");

        Usuario_com_exemplar = conexao.prepareStatement("select distinct(u.nome) from emprestimo e join usuarios u on e.id_user = u.id where e.id_user = ?");

    }
    
    public static EmprestimoDAO getInstance() throws ClassNotFoundException, SQLException {
        if(instance == null) {
            instance = new EmprestimoDAO();
        }
        return instance;
    }

    public String Usuario_com_exemplarFunc(int id_usuario) {
        try {
            Usuario_com_exemplar.setInt(1,id_usuario);
            ResultSet rs = Usuario_com_exemplar.executeQuery();
            if(rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar novo id na tabela emprestimo");
        }
        return "ERRO";
    }

    public String Usuario_que_esta_com_Exemplar(int id_exemplar) {
        int id_usuario;
        try {
            localiza_usuario_exemplar.setInt(1,id_exemplar);
            ResultSet rs = localiza_usuario_exemplar.executeQuery();
            if(rs.next()) {
                id_usuario = rs.getInt(1);
                return Usuario_com_exemplarFunc(id_usuario);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao procurar qual usuario esta com um determinado exemplar");
        }
        return "ERRO";
    }


    public Boolean esta_Disponivel_Exemplar(int cod_Exemplar) throws SelectException {
        try{
            Verifica_Se_Exemplar_esta_Emprestado.setInt(1, cod_Exemplar);
            ResultSet rs = Verifica_Se_Exemplar_esta_Emprestado.executeQuery();

            while(rs.next()) {
                int count = rs.getInt(1);
                if(count>0) {
                    return true; //se deu 1, está disponível, então o exemplar esta disponivel -> SIM
                } else {
                    return false; //se deu 0, está emprestado ou nao existe.
                }
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao procurar se o Exemplar está emprestado");
        }
        return false; //se der erro, esta emprestado ou nao existe.
    }

    public Boolean Verifica_Se_Exemplar_Existe_Tabela_Emprestimo(int cod_Exemplar) throws SelectException {
        try{
            Verifica_Se_Exemplar_Existe_Tabela_Emprestimo.setInt(1, cod_Exemplar);
            ResultSet rs = Verifica_Se_Exemplar_Existe_Tabela_Emprestimo.executeQuery();

            while(rs.next()) {
                int count = rs.getInt(1);
                if(count>0) {
                    return true; //Exemplar existe na tabela de emprestimo
                } else {
                    return false; //Exemplar não existe na tabela de emprestimo, logo está disponível.
                }
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao procurar se o Exemplar está emprestado");
        }
        return false; //se der erro, esta emprestado ou nao existe.
    }

    

    public int selectNewId() {
        try {
            ResultSet rs = selectNewId.executeQuery();
            if(rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar novo id na tabela emprestimo");
        }
        return 0;
    }

    public int insert(Emprestimo Emprestimo_dados) {
        try {
             insert.setInt(1, Emprestimo_dados.getUsuario().getId());
             insert.setInt(2, Emprestimo_dados.getExemplar().getId());
             insert.executeQuery();
            return 1;

        } catch (SQLException e) {
            String errorMessage = e.getMessage();
            //String errorMessage = e.getMessage();
            //System.out.println("ERRO SQL: " + errorMessage);
            // Extrair a mensagem de erro principal
            int endIndex = errorMessage.indexOf("\n");
            String error = (endIndex != -1) ? errorMessage.substring(0, endIndex) : errorMessage;

            if (error != null) {
                System.out.println("Erro SQL: " + error);
            }
            return 0;
            
            
        } 
        
    }

    //select e.id,u.nome, l.titulo from emprestimo e left join usuarios u on e.id_user = u.id join exemplares ex on e.id = ex.id join livros l on ex.id_livro = l.id
    public List<Emprestimo> selectAll() throws SelectException {

        List<Emprestimo> lista_de_Emprestimos = new LinkedList<>();
        
        try{
            ResultSet rs = selectAll.executeQuery();

            while(rs.next()) {
                int id_emprestimo = rs.getInt(1);
                String nome_Usuario = rs.getString(2); 
                String titulo_Livro = rs.getString(3);
                Usuario usuario = new Usuario(nome_Usuario,null,null,null,null);
                Livro livro = new Livro(null, titulo_Livro,null , null, null);
                Exemplar exemplar = new Exemplar(livro, null);
                Emprestimo emprestimo = new Emprestimo(id_emprestimo,usuario, exemplar);

                lista_de_Emprestimos.add(emprestimo);
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao listar Emprestimos ");
        }
        return lista_de_Emprestimos;
    }


    
    public int selectOne(int id_exemplar) throws SelectException{
    	
    	ResultSet rs;
    	int id_user;
    	
    	try {
    		
    		findUser.setInt(1, id_exemplar);
    		rs = findUser.executeQuery();
    		id_user = rs.getInt(1);
    		
    	} catch (SQLException e){
            throw new SelectException("Erro ao procurar Usuario");
        }
		return id_user;
    	
    }

   

}

