package school.sptech;

import java.sql.Connection;


public class Main {
      public static void main(String[] args) {


        LerPersistirDados persistirDados = new LerPersistirDados();

        persistirDados.inserirDadosInflacao("inflacao.xlsx");
        persistirDados.inserirDadosSelic("selic.xlsx");
        persistirDados.inserirDadosPib("2t2025_tabelas_site-pib-trimestral.xlsx");
        persistirDados.inserirDadosPibSetor("2t2025_tabelas_site-pib-trimestral.xlsx");
        persistirDados.inserirDadosPibRegionalSP("sp_pib.xlsx");
        persistirDados.inserirDadosPopulacao("populcaoFiltrado.xlsx");

        // Slack
        SlackNotifier slack = new SlackNotifier();

        System.out.println("=======================================================> chamando metodo do slack");
        slack.enviar();

    }
}