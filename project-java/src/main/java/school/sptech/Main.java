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

        List<String> arquivosCsv = List.of(
                "inflacao.xlsx",
                "populacao.xlsx",
                "ipeaData_PIB_ConstrucaoCivil.xlsx",
                "selic.xlsx"
        );

        LerPersistirDados persistirDados = new LerPersistirDados();

        for (String csv : arquivosCsv) {
            if(csv == "inflacao.csv"){
                persistirDados.inserirDadosInflacao(csv);
            } else if (csv == "selic.csv") {
                persistirDados.inserirDadosSelic(csv);
            }
        }

    }
}