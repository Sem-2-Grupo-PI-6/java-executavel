package school.sptech.slack;

import org.springframework.jdbc.core.JdbcTemplate;
import school.sptech.Conexao;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
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

    public Double buscarTaxaSelicAtual() {
        Double taxaSelicAtual = jdbcTemplate
                .queryForObject("SELECT valorTaxa FROM tblSelic AS s\n" +
                        "JOIN tblLogArquivos AS l\n" +
                        "ON l.tblSelic_idtblSelic = s.idtblSelic \n" +
                        "WHERE DATE(dataHoraLeitura) = CURRENT_DATE;", (Double.class));
        return taxaSelicAtual;
    }

    public Double buscarTaxaSelicAnterior() {
        LocalDate dataAtual = jdbcTemplate
                .queryForObject("SELECT DATE(dataHoraLeitura) FROM tblLogArquivos ORDER BY dataHoraLeitura DESC LIMIT 1;", (LocalDate.class));

        LocalDate dataAnterior = dataAtual.minusDays(1);

        Double taxaSelicAnterior = jdbcTemplate
                .queryForObject("SELECT valorTaxa FROM tblSelic AS s\n" +
                        "JOIN tblLogArquivos AS l\n" +
                        "ON l.tblSelic_idtblSelic = s.idtblSelic \n" +
                        "WHERE DATE(dataHoraLeitura) = ?;", (Double.class), dataAnterior);
        return taxaSelicAnterior;
    }
    public Double buscarTaxaSelicAnteAnterior() {
        LocalDate dataAtual = jdbcTemplate
                .queryForObject("SELECT DATE(dataHoraLeitura) FROM tblLogArquivos ORDER BY dataHoraLeitura DESC LIMIT 1;", (LocalDate.class));

        LocalDate dataAnterior = dataAtual.minusDays(2);

        Double taxaSelicAnterior = jdbcTemplate
                .queryForObject("SELECT valorTaxa FROM tblSelic AS s\n" +
                        "JOIN tblLogArquivos AS l\n" +
                        "ON l.tblSelic_idtblSelic = s.idtblSelic \n" +
                        "WHERE DATE(dataHoraLeitura) = ?;", (Double.class), dataAnterior);
        return taxaSelicAnterior;
    }

    public Double buscarPibConstrucaoCivilAtual() {
        Double pibConstrucaoCivilAtual = jdbcTemplate
                .queryForObject("SELECT pibSP FROM tblPibRegionalSP AS p\n" +
                        "JOIN tblLogArquivos AS l\n" +
                        "ON l.tblSelic_idtblSelic = p.idtblPibRegionalSP\n" +
                        "WHERE DATE(dataHoraLeitura) = CURRENT_DATE;", (Double.class));
        return pibConstrucaoCivilAtual;
    }

    public Double buscarPibConstrucaoCivilAnterior() {
        LocalDate dataAtual = jdbcTemplate
                .queryForObject("SELECT DATE(dataHoraLeitura) FROM tblLogArquivos ORDER BY dataHoraLeitura DESC LIMIT 1;", (LocalDate.class));

        LocalDate dataAnterior = dataAtual.minusDays(1);

        Double pibConstrucaoCivilAnterior = jdbcTemplate
                .queryForObject("SELECT pibSP FROM tblPibRegionalSP AS p\n" +
                        "JOIN tblLogArquivos AS l\n" +
                        "ON l.tblSelic_idtblSelic = p.idtblPibRegionalSP\n" +
                        "WHERE DATE(dataHoraLeitura) = ?;", (Double.class), dataAnterior);
        return pibConstrucaoCivilAnterior;
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

        Double diferencaSelic = null;
        if (notificacaoAumentoSelic == 1) {
            if (buscarTaxaSelicAtual() > buscarTaxaSelicAnterior() && buscarTaxaSelicAnterior() > buscarTaxaSelicAnteAnterior()) {
                diferencaSelic = buscarTaxaSelicAtual() - buscarTaxaSelicAnteAnterior();
                mensagem = "🚨ALERTA🚨\n" +
                        "A taxa Selic aumentou de " + buscarTaxaSelicAnteAnterior() + " para " + buscarTaxaSelicAnterior() + "e depois para " + buscarTaxaSelicAtual() + ".\n" +
                        "A diferença da taxa Selic dos ultimos dois dias para a atual seria de + " + diferencaSelic + " na taxa.";
            }else if (buscarTaxaSelicAtual() > buscarTaxaSelicAnterior()) {
                diferencaSelic = buscarTaxaSelicAtual() - buscarTaxaSelicAnterior();
                mensagem = "🚨ALERTA🚨\n" +
                        "A taxa Selic aumentou de " + buscarTaxaSelicAnterior() + " para " + buscarTaxaSelicAtual() + ".\n" +
                        "A diferença da taxa Selic anterior para a atual seria de + " + diferencaSelic + " na taxa.";
            }

            if (buscarTaxaSelicAtual() < buscarTaxaSelicAnterior()) {
                diferencaSelic = buscarTaxaSelicAnterior() - buscarTaxaSelicAtual();
                mensagem = "🚨ALERTA🚨\n" +
                        "A taxa Selic diminuiu de " + buscarTaxaSelicAnterior() + " para " + buscarTaxaSelicAtual() + ".\n" +
                        "A diferença da taxa Selic anterior para a atual seria de - " + diferencaSelic + " na taxa.";
            }

            if (buscarTaxaSelicAtual() == buscarTaxaSelicAnterior()) {
                mensagem = "🚨ALERTA🚨\n" +
                        "Nenhuma mudança na taxa Selic, ela se manteve em: " + buscarTaxaSelicAtual();
            }

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

        Double diferencaPib = null;
        if (notificacaoCrescimentoPib == 1) {

            if (buscarPibConstrucaoCivilAtual() > buscarPibConstrucaoCivilAnterior()) {
                diferencaPib = buscarPibConstrucaoCivilAtual() - buscarPibConstrucaoCivilAnterior();
                mensagem = "🚨ALERTA🚨\n" +
                        "O PIB no setor de Construção Civil aumentou de " + buscarPibConstrucaoCivilAnterior() + " para " + buscarPibConstrucaoCivilAtual() + ".\n" +
                        "A diferença do PIB anterior para o atual seria de + " + diferencaPib + " no PIB.";
            }

            if (buscarPibConstrucaoCivilAtual() < buscarPibConstrucaoCivilAnterior()) {
                diferencaSelic = buscarTaxaSelicAtual() - buscarPibConstrucaoCivilAnterior();
                mensagem = "🚨ALERTA🚨\n" +
                        "O PIB no setor de Construção Civil diminuiu de " + buscarPibConstrucaoCivilAnterior() + " para " + buscarPibConstrucaoCivilAtual() + ".\n" +
                        "A diferença do PIB anterior para o atual seria de - " + diferencaPib + " no PIB.";
            }

            if (buscarPibConstrucaoCivilAtual() == buscarPibConstrucaoCivilAnterior()) {
                mensagem = "🚨ALERTA🚨\n" +
                        "Nenhuma mudança no PIB do setor de Construção Civil, ele se manteve em: " + buscarPibConstrucaoCivilAtual();
            }

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