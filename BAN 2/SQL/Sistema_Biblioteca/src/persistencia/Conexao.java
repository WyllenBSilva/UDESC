package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {


    private static Connection conexao = null;

    private Conexao() {

    }

    public static Connection getConexao() throws ClassNotFoundException, SQLException {
        if(conexao==null) {
            String url = "jdbc:postgresql://localhost:5432/Biblioteca";
            String usuario = "postgres";
            String senha = "javaPOO2022";
            Class.forName("org.postgresql.Driver");
            conexao = DriverManager.getConnection(url,usuario,senha);
        }
        return conexao;
    }


}
 