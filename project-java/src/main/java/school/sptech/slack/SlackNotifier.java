package school.sptech.slack;

import org.springframework.jdbc.core.JdbcTemplate;
import school.sptech.Conexao;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class SlackNotifier {

    private final Conexao conexao = new Conexao();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(conexao.getConexao());
    private final String webhookAlerta = jdbcTemplate.queryForObject(
            "SELECT webhook FROM tblCanalWebhook WHERE nomeCanal = 'alertas';", (String.class));
    private final String webhookMaiorPopulacao = jdbcTemplate.queryForObject(
            "SELECT webhook FROM tblCanalWebhook WHERE nomeCanal = 'notificação-zona-com-maior-quantidade-população';", (String.class));
    private final String webhookAumentoSelic = jdbcTemplate.queryForObject(
            "SELECT webhook FROM tblCanalWebhook WHERE nomeCanal = 'notificação-aumento-taxa-selic';", (String.class));
    private final String webhookCrescimentoPib = jdbcTemplate.queryForObject(
            "SELECT webhook FROM tblCanalWebhook WHERE nomeCanal = 'notificação-crescimento-pib';", (String.class));

    public SlackNotifier() {
        try (Connection conn = conexao.getConexao().getConnection()) {
            System.out.println("Conexão estabelecida com sucesso!");
        } catch (Exception e) {
            System.err.println("Falha na conexão: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String buscarZonaPorMaiorQtdPopulacao() {
        String zonaComMaiorQtdPopulacao = jdbcTemplate
                .queryForObject("SELECT z.nome FROM tblZona AS z \n" +
                        "\tJOIN tblPopulacao AS p \n" +
                        "\tON p.tblZona_idZona = z.idZona \n" +
                        "    GROUP BY z.nome\n" +
                        "    ORDER BY SUM(p.qtdPopulacao) DESC LIMIT 1;", (String.class));
        return zonaComMaiorQtdPopulacao;
    }

    public void enviar(String mensagem) {

        Integer receberNotificacao = jdbcTemplate.queryForObject(
                "SELECT receberNotificacao FROM tblUsuario;", (Integer.class));
        Integer notificacaoMaiorPopulacao = jdbcTemplate.queryForObject(
                "SELECT maiorPopulacao FROM tblSlack AS s\n" +
                        "\tJOIN tblUsuario AS u\n" +
                        "    ON u.fkSlack = s.idSlack;", (Integer.class));
        Integer notificacaoAumentoSelic = jdbcTemplate.queryForObject(
                "SELECT aumentoSelic FROM tblSlack AS s\n" +
                        "\tJOIN tblUsuario AS u\n" +
                        "    ON u.fkSlack = s.idSlack;", (Integer.class));
        Integer notificacaoCrescimentoPib = jdbcTemplate.queryForObject(
                "SELECT crescimentoPib FROM tblSlack AS s\n" +
                        "\tJOIN tblUsuario AS u\n" +
                        "    ON u.fkSlack = s.idSlack;", (Integer.class));
        Integer notificacaoAlertaError = jdbcTemplate.queryForObject(
                "SELECT alertaError FROM tblSlack AS s\n" +
                        "\tJOIN tblUsuario AS u\n" +
                        "    ON u.fkSlack = s.idSlack;", (Integer.class));
        Integer notificacaoAlertaWarning = jdbcTemplate.queryForObject(
                "SELECT alertaWarning FROM tblSlack AS s\n" +
                        "\tJOIN tblUsuario AS u\n" +
                        "    ON u.fkSlack = s.idSlack;", (Integer.class));
        Integer notificacaoAlertaInfo = jdbcTemplate.queryForObject(
                "SELECT alertaInfo FROM tblSlack AS s\n" +
                        "\tJOIN tblUsuario AS u\n" +
                        "    ON u.fkSlack = s.idSlack;", (Integer.class));

        if (receberNotificacao == 1) {
            try {
                String json = "{\"text\": \"" + mensagem + "\"}";

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(webhookAlerta))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();

                HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (notificacaoMaiorPopulacao == 1) {
            mensagem = "🚨ALERTA🚨\n" +
                    "A zona com a maior quantidade de população é: " + buscarZonaPorMaiorQtdPopulacao();
            try {
                String json = "{\"text\": \"" + mensagem + "\"}";

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(webhookMaiorPopulacao))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();

                HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (notificacaoAumentoSelic == 1) {
            try {
                String json = "{\"text\": \"" + mensagem + "\"}";

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(webhookAumentoSelic))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();

                HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (notificacaoCrescimentoPib == 1) {
            try {
                String json = "{\"text\": \"" + mensagem + "\"}";

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(webhookCrescimentoPib))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();

                HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}