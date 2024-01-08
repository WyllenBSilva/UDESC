package negocio;

import java.sql.SQLException;
import java.util.List;

import dados.*;
import exceptions.*;
import persistencia.*;

public class Sistema {

    private static LivroDAO livro_DAO;
    private static BibliotecarioDAO bibliotecario_DAO;
    private static AssistenteDAO assistente_DAO;
    private static UsuarioDAO usuario_DAO;
    private static ExemplarDAO exemplar_DAO;
    private static EmprestimoDAO emprestimo_DAO;
    private static ReservaDAO reserva_DAO;
    //se adicionar um static, é necessário pegar uma instancia abaixo:

    public Sistema() throws ClassNotFoundException, SQLException {
        livro_DAO = LivroDAO.getInstance();
        bibliotecario_DAO = BibliotecarioDAO.getInstance();
        assistente_DAO = AssistenteDAO.getInstance();
        usuario_DAO = UsuarioDAO.getInstance();
        exemplar_DAO = ExemplarDAO.getInstance();
        emprestimo_DAO = EmprestimoDAO.getInstance();
        reserva_DAO = ReservaDAO.getInstance();
    }


    public void cadastrarBibliotecario(Bibliotecario Biblio_dados) throws InsertException {
        bibliotecario_DAO.insert(Biblio_dados);
    }
    
    public void cadastrarAssistente(Assistente assistente) throws InsertException {
        assistente_DAO.insert(assistente);
    }
    
    public void cadastrarUsuario(Usuario usuario) throws InsertException{
        usuario_DAO.insert(usuario);
    }
    
    public void cadastrarLivro(Livro livro) throws InsertException {
        livro_DAO.insert(livro);
    }
    
    public void cadastrarExemplarLivro(Exemplar exemplar) throws InsertException {
        exemplar_DAO.insert(exemplar);
    }

    public int cadastrar_Emprestimo(Emprestimo emprestimo) throws SQLException {
        return emprestimo_DAO.insert(emprestimo);
    }

    public int Reservar_Livro(Reserva reserva) throws SQLException {
        return reserva_DAO.insert(reserva);
    }

    
    public void realizarEmprestimoExemplar() {
        // Implementação para realizar o empréstimo de um exemplar

        
    }
    
    
    public void localizarUsuarioComExemplar() {
        // Implementação para localizar o usuário que está com um exemplar
    }


    public List<Usuario> listar_Todos_Usuarios() throws SelectException {
        return usuario_DAO.selectAll();
    }

    public List<Livro> ListarTodosOsLivros() throws SelectException {
       
        return livro_DAO.selectAll();
    }
    public List<Exemplar> ListarTodosOsExemplares() throws SelectException {
       
        return exemplar_DAO.selectAll();
    }

    public List<Exemplar> Listar_Nome_Livro_Exemplar() throws SelectException {
       
        return exemplar_DAO.Nome_Livro_Exemplar();
    }


    public List<Assistente> listarTodoAssistentes() throws SelectException {
        return assistente_DAO.selectAll();
    }

    public List<Bibliotecario> listarTodosBibliotecarios() throws SelectException {
        return bibliotecario_DAO.selectAll();
    }

    public List<Emprestimo> listarTodos_Emprestimos() throws SelectException {
        return emprestimo_DAO.selectAll();
    }

    public boolean Verifica_Se_Assistente_Existe(int cod_assistente) throws SelectException {
        return assistente_DAO.AssisteExiste(cod_assistente);
    }

    public boolean Verifica_Se_Livro_Existe(int cod_livro) throws SelectException {
        return livro_DAO.Livro_Existe(cod_livro);
    }

    public boolean Verifica_Se_Usuario_Existe(int cod_usuario) throws SelectException {
        return usuario_DAO.Usuario_Existe(cod_usuario);
    }

    public boolean Verifica_Se_Exemplar_Existe(int cod_Exemplar) throws SelectException {
        return exemplar_DAO.Exemplar_Existe(cod_Exemplar);
    }

    public boolean esta_Disponivel_Exemplar(int cod_Exemplar) throws SelectException {
        return emprestimo_DAO.esta_Disponivel_Exemplar(cod_Exemplar);
    }

    public boolean Verifica_Se_Exemplar_Existe_Tabela_Emprestimo(int cod_Exemplar) throws SelectException {
        return emprestimo_DAO.Verifica_Se_Exemplar_Existe_Tabela_Emprestimo(cod_Exemplar);
    }
    
    public String Usuario_que_esta_com_Exemplar(int id_Exemplar) throws SelectException {
        return emprestimo_DAO.Usuario_que_esta_com_Exemplar(id_Exemplar);
    }
    
    
    
}
