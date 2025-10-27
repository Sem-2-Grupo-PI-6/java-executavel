package school.sptech;
import java.sql.Connection;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Conexao conexao = new Conexao();
        try (Connection conn = conexao.getConexao().getConnection()) {
            System.out.println("Conexão estabelecida com sucesso!");
        } catch (Exception e) {
            System.err.println("Falha na conexão: " + e.getMessage());
            e.printStackTrace();
        }
        LerPersistirDados persistirDados = new LerPersistirDados();

        persistirDados.inserirDadosInflacao("inflacao.csv");
        persistirDados.inserirDadosSelic("selic.csv");
        persistirDados.inserirDadosPibConstrucaoCivil("ipeaData_PIB_ConstrucaoCivil.csv");
        persistirDados.inserirDadosPib("pib.csv");

        
    }
}