package school.sptech;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

public class Conexao {

    private DataSource conexao;

    public Conexao(){
        DriverManagerDataSource driver = new DriverManagerDataSource();

        driver.setUrl("jdbc:mysql://localhost:3306/sixtech?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        driver.setUsername("root");
        driver.setPassword("Kcaio121922032521!");
        driver.setDriverClassName("com.mysql.cj.jdbc.Driver");

        this.conexao = driver;
    }

    public DataSource getConexao(){
        return conexao;
    }

}