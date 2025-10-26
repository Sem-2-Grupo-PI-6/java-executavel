
package school.sptech;

import com.opencsv.CSVReader;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class LerPersistirDados {

    private final Conexao conexao = new Conexao();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(conexao.getConexao());
    private final String bucketName = "s3-sixtech";
    private final Region region = Region.US_EAST_1;
    private final S3Client s3Client;

    public LerPersistirDados() {
        this.s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    public void inserirDadosInflacao(String key) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println("[" + timestamp + "] ⏳ Iniciando leitura do arquivo CSV: " + key);

        try (InputStream inputStream = baixarArquivo(key);
             InputStreamReader isr = new InputStreamReader(inputStream);
             CSVReader csvReader = new CSVReader(isr)) {

            String[] linha;
            int count = 0;
            while ((linha = csvReader.readNext()) != null) {
                if (linha.length >= 2 && linha[0] != null && linha[1] != null &&
                        !linha[0].isEmpty() && !linha[1].isEmpty()) {
                    try {

                        String dataApuracao = linha[0];
                        String valor = linha[1].replace(",", ".");

                        Double taxaApuracao = Double.parseDouble(valor);


                        System.out.println("Como esta após tratamento:  Data:" + dataApuracao +" Taxa:"+ taxaApuracao);
                        jdbcTemplate.update(
                                "INSERT INTO inflacao (taxaInflacao, dataApuracao) VALUES (?, ?)",
                                taxaApuracao,
                                dataApuracao
                        );

                        List<Inflacao> inflacao = jdbcTemplate
                                .query("SELECT * FROM inflacao ORDER BY id DESC LIMIT 1;", new BeanPropertyRowMapper<>(Inflacao.class));

                        jdbcTemplate.update(
                                "INSERT INTO logInflacao (idInflacao, descricao) VALUES (?, ?)",
                                inflacao.getFirst().getId(),
                                "Os registros "+ taxaApuracao +" e "+ dataApuracao +" foram inseridos"
                        );
                        count++;
                    } catch (Exception e) {
                        System.err.println("Linha inválida: " + Arrays.toString(linha) + " -> " + e.getMessage());
                    }
                }
            }

            System.out.println("[" + timestamp + "]  Inserção de " + count + " registros concluída com sucesso!");

        } catch (DataAccessException e) {
            throw new RuntimeException("[" + timestamp + "]  Erro no banco de dados: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("[" + timestamp + "]  Erro de I/O ao processar arquivo do S3: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("[" + timestamp + "]  Erro inesperado: " + e.getMessage(), e);
        }

    }

    public void inserirDadosSelic(String key) {
    String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    System.out.println("[" + timestamp + "] ⏳ Iniciando leitura do arquivo CSV: " + key);

    try (InputStream inputStream = baixarArquivo(key);
         InputStreamReader isr = new InputStreamReader(inputStream);
         CSVReader csvReader = new CSVReader(isr)) {

        String[] linha;
        int count = 0;
        while ((linha = csvReader.readNext()) != null) {
            if (linha.length >= 2 && linha[0] != null && linha[1] != null &&
                !linha[0].isEmpty() && !linha[1].isEmpty()) {
                try {
                    String dataApuracao = linha[0];
                    String valor = linha[1].replace(",", ".");
                    Double taxaSelic = Double.parseDouble(valor);

                    System.out.println("Como está após tratamento: Data=" + dataApuracao + " | Taxa=" + taxaSelic);

                    jdbcTemplate.update(
                        "INSERT INTO selic (taxaSelic, dataApuracao) VALUES (?, ?)",
                        taxaSelic,
                        dataApuracao
                    );

                    List<Selic> selic = jdbcTemplate.query(
                        "SELECT * FROM selic ORDER BY id DESC LIMIT 1",
                        new BeanPropertyRowMapper<>(Selic.class)
                    );

                    jdbcTemplate.update(
                        "INSERT INTO logSelic (idSelic, descricao) VALUES (?, ?)",
                        selic.getFirst().getId(),
                        "Registro " + taxaSelic + " e " + dataApuracao + " inseridos com sucesso"
                    );

                    count++;
                } catch (Exception e) {
                    System.err.println("Linha inválida: " + Arrays.toString(linha) + " -> " + e.getMessage());
                }
            }
        }
        System.out.println("[" + timestamp + "] Inserção de " + count + " registros concluída com sucesso!");

    } catch (DataAccessException e) {
        throw new RuntimeException("[" + timestamp + "]  Erro no banco de dados: " + e.getMessage(), e);
    } catch (IOException e) {
        throw new RuntimeException("[" + timestamp + "]  Erro de I/O ao processar arquivo do S3: " + e.getMessage(), e);
    } catch (Exception e) {
        throw new RuntimeException("[" + timestamp + "]  Erro inesperado: " + e.getMessage(), e);
    }
}

public void inserirDadosPibConstrucaoCivil(String key) {
    String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    System.out.println("[" + timestamp + "] ⏳ Iniciando leitura do arquivo CSV: " + key);

    try (InputStream inputStream = baixarArquivo(key);
         InputStreamReader isr = new InputStreamReader(inputStream);
         CSVReader csvReader = new CSVReader(isr)) {

        String[] linha;
        int count = 0;
        while ((linha = csvReader.readNext()) != null) {
            if (linha.length >= 2 && linha[0] != null && linha[1] != null &&
                !linha[0].isEmpty() && !linha[1].isEmpty()) {
                try {
                    String dataApuracao = linha[0];
                    String dataApuracaoTradada = dataApuracao.split(" ")[0];

                    String valor = linha[1].replace(",", "");
                    Double valorPib = Double.parseDouble(valor);

                    System.out.println("Após tratamento: Data=" + dataApuracaoTradada + " | Valor PIB=" + valorPib);

                    jdbcTemplate.update(
                        "INSERT INTO pibConstrucaoCivil (valorPib, dataApuracao) VALUES (?, ?)",
                        valorPib,
                        dataApuracaoTradada
                    );

                    List<PibConstrucaoCivil> pib = jdbcTemplate.query(
                        "SELECT * FROM pibConstrucaoCivil ORDER BY id DESC LIMIT 1",
                        new BeanPropertyRowMapper<>(PibConstrucaoCivil.class)
                    );

                    jdbcTemplate.update(
                        "INSERT INTO logPibConstrucaoCivil (idPibConstrucaoCivil, descricao) VALUES (?, ?)",
                        pib.getFirst().getId(),
                        "Registro " + valorPib + " e " + dataApuracaoTradada + " inseridos com sucesso"
                    );

                    count++;
                } catch (Exception e) {
                    System.err.println("Linha inválida: " + Arrays.toString(linha) + " -> " + e.getMessage());
                }
            }
        }

        System.out.println("[" + timestamp + "] Inserção de " + count + " registros concluída com sucesso!");

    } catch (DataAccessException e) {
        throw new RuntimeException("[" + timestamp + "]  Erro no banco de dados: " + e.getMessage(), e);
    } catch (IOException e) {
        throw new RuntimeException("[" + timestamp + "]  Erro de I/O ao processar arquivo do S3: " + e.getMessage(), e);
    } catch (Exception e) {
        throw new RuntimeException("[" + timestamp + "]  Erro inesperado: " + e.getMessage(), e);
    }
}

public void inserirDadosPib(String key) {
    String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    System.out.println("[" + timestamp + "] ⏳ Iniciando leitura do arquivo CSV: " + key);

    try (InputStream inputStream = baixarArquivo(key);
         InputStreamReader isr = new InputStreamReader(inputStream);
         CSVReader csvReader = new CSVReader(isr)) {

        String[] linha;
        int count = 0;
        while ((linha = csvReader.readNext()) != null) {
            if (linha.length >= 2 && linha[0] != null && linha[1] != null &&
                !linha[0].isEmpty() && !linha[1].isEmpty()) {
                try {
                    String valorStr = linha[0].replace(",", ".");
                    String idZonaStr = linha[1];

                    Integer valor = (int) Double.parseDouble(valorStr);
                    Integer idZona = Integer.parseInt(idZonaStr);

                    jdbcTemplate.update(
                        "INSERT INTO pib (valor, idZona) VALUES (?, ?)",
                        valor,
                        idZona
                    );

                    count++;
                } catch (Exception e) {
                    System.err.println("Linha inválida: " + Arrays.toString(linha) + " -> " + e.getMessage());
                }
            }
        }

        System.out.println("[" + timestamp + "] Inserção de " + count + " registros de PIB concluída com sucesso!");

    } catch (DataAccessException e) {
        throw new RuntimeException("[" + timestamp + "]  Erro no banco de dados: " + e.getMessage(), e);
    } catch (IOException e) {
        throw new RuntimeException("[" + timestamp + "]  Erro de I/O ao processar arquivo do S3: " + e.getMessage(), e);
    } catch (Exception e) {
        throw new RuntimeException("[" + timestamp + "]  Erro inesperado: " + e.getMessage(), e);
    }
}

public void inserirDadosPopulacao(String key) {
    String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    System.out.println("[" + timestamp + "] ⏳ Iniciando leitura do arquivo CSV: " + key);

    try (InputStream inputStream = baixarArquivo(key);
         InputStreamReader isr = new InputStreamReader(inputStream);
         CSVReader csvReader = new CSVReader(isr)) {

        String[] linha;
        int count = 0;
        while ((linha = csvReader.readNext()) != null) {
            // Esperado: ano, codigoIbge, municipio, qtdPopulacao, homens, mulheres, razaoSexo, idadeMedia, densidadeDemografico, idZona
            if (linha.length >= 10) {
                try {
                    String ano = linha[0];
                    String codigoIbge = linha[1];
                    String municipio = linha[2];
                    Integer qtdPopulacao = Integer.parseInt(linha[3]);
                    Integer homens = Integer.parseInt(linha[4]);
                    Integer mulheres = Integer.parseInt(linha[5]);
                    Double razaoSexo = Double.parseDouble(linha[6].replace(",", "."));
                    Double idadeMedia = Double.parseDouble(linha[7].replace(",", "."));
                    Double densidadeDemografico = Double.parseDouble(linha[8].replace(",", "."));
                    Integer idZona = Integer.parseInt(linha[9]);

                    jdbcTemplate.update(
                        "INSERT INTO populacao (ano, codigoIbge, municipio, qtdPopulacao, homens, mulheres, razaoSexo, idadeMedia, densidadeDemografico, idZona) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        ano, codigoIbge, municipio, qtdPopulacao, homens, mulheres,
                        razaoSexo, idadeMedia, densidadeDemografico, idZona
                    );

                    List<Populacao> pop = jdbcTemplate.query(
                        "SELECT * FROM populacao ORDER BY id DESC LIMIT 1",
                        new BeanPropertyRowMapper<>(Populacao.class)
                    );

                    jdbcTemplate.update(
                        "INSERT INTO logPopulacao (idPopulacao, descricao) VALUES (?, ?)",
                        pop.getFirst().getId(),
                        "Registro de população (" + municipio + ") inserido com sucesso."
                    );

                    count++;
                } catch (Exception e) {
                    System.err.println("Linha inválida: " + Arrays.toString(linha) + " -> " + e.getMessage());
                }
            }
        }

        System.out.println("[" + timestamp + "] Inserção de " + count + " registros de população concluída!");

    } catch (DataAccessException e) {
        throw new RuntimeException("[" + timestamp + "]  Erro no banco de dados: " + e.getMessage(), e);
    } catch (IOException e) {
        throw new RuntimeException("[" + timestamp + "]  Erro de I/O ao processar arquivo do S3: " + e.getMessage(), e);
    } catch (Exception e) {
        throw new RuntimeException("[" + timestamp + "]  Erro inesperado: " + e.getMessage(), e);
    }
}

public void inserirDadosZona(String key) {
    String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    System.out.println("[" + timestamp + "] ⏳ Iniciando leitura do arquivo CSV: " + key);

    try (InputStream inputStream = baixarArquivo(key);
         InputStreamReader isr = new InputStreamReader(inputStream);
         CSVReader csvReader = new CSVReader(isr)) {

        String[] linha;
        int count = 0;
        while ((linha = csvReader.readNext()) != null) {
            if (linha.length >= 1 && linha[0] != null && !linha[0].isEmpty()) {
                try {
                    String nome = linha[0];
                    jdbcTemplate.update("INSERT INTO zona (nome) VALUES (?)", nome);
                    count++;
                } catch (Exception e) {
                    System.err.println("Linha inválida: " + Arrays.toString(linha) + " -> " + e.getMessage());
                }
            }
        }

        System.out.println("[" + timestamp + "] Inserção de " + count + " zonas concluída!");

    } catch (DataAccessException e) {
        throw new RuntimeException("[" + timestamp + "]  Erro no banco de dados: " + e.getMessage(), e);
    } catch (IOException e) {
        throw new RuntimeException("[" + timestamp + "]  Erro de I/O ao processar arquivo do S3: " + e.getMessage(), e);
    } catch (Exception e) {
        throw new RuntimeException("[" + timestamp + "]  Erro inesperado: " + e.getMessage(), e);
    }
}

    private InputStream baixarArquivo(String key) throws IOException {
        System.out.println(" Baixando do S3: " + bucketName + "/" + key);

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try {
            ResponseInputStream<GetObjectResponse> response = s3Client.getObject(request);
            System.out.println("Arquivo CSV carregado do S3 com sucesso!");
            return response;
        } catch (S3Exception e) {
            System.err.println("Erro ao baixar do S3: " + e.awsErrorDetails().errorMessage());
            throw new IOException("Falha ao obter InputStream do S3", e);
        }
    }

    public void fecharS3() {
        s3Client.close();
    }
}
