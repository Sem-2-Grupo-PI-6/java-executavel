package school.sptech;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

public class Conexao {

    private DataSource conexao;

    public Conexao(){
        DriverManagerDataSource driver = new DriverManagerDataSource();

        driver.setUrl("jdbc:mysql://44.223.127.232:3306/sixtech?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        driver.setUsername("root");
        driver.setPassword("urubu100");
        driver.setDriverClassName("com.mysql.cj.jdbc.Driver");

        this.conexao = driver;
    }

    public DataSource getConexao(){
        return conexao;
    }

}