package school.sptech;

import org.springframework.jdbc.core.JdbcTemplate;
import school.sptech.slack.SlackNotifier;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.time.Duration;
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

//        LerPersistirDados persistirDados = new LerPersistirDados();
//
//        //persistirDados.inserirDadosPib("2t2025_tabelas_site-pib-trimestral.xlsx");
//        persistirDados.inserirDadosPibSetor("2t2025_tabelas_site-pib-trimestral.xlsx");


        // Slack
        SlackNotifier slack = new SlackNotifier();

        slack.buscarZonaPorMaiorQtdPopulacao();
        slack.enviar("🚨ALERTA🚨");
        slack.buscarTaxaSelicAtual();
        slack.buscarTaxaSelicAnterior();
        slack.buscarPibConstrucaoCivilAtual();
        slack.buscarPibConstrucaoCivilAnterior();
    }
}