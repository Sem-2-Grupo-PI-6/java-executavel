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

        persistirDados.inserirDadosInflacao("inflacao.xlsx");
        // persistirDados.inserirDadosSelic("selic.xlsx");
        persistirDados.inserirDadosPib("2t2025_tabelas_site-pib-trimestral.xlsx");
        persistirDados.inserirDadosPibSetor("2t2025_tabelas_site-pib-trimestral.xlsx");
        persistirDados.inserirDadosPibRegionalSP("sp_pib.xlsx");
        persistirDados.inserirDadosPopulacao("populcaoFiltrado.xlsx");
    }
}