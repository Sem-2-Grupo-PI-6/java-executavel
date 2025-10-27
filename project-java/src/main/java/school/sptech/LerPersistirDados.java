package school.sptech;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.*;

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
        System.out.println("[" + timestamp + "] ⏳ Iniciando leitura do arquivo XLSX: " + key);

        try (InputStream inputStream = baixarArquivo(key);
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            int count = 0;

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                Cell cData = row.getCell(0);
                Cell cValor = row.getCell(1);

                if (cData == null || cValor == null) continue;

                try {
                    String dataApuracao = cData.toString().trim();
                    String valor = cValor.toString().replace(",", ".");
                    Double taxaApuracao = Double.parseDouble(valor);

                    jdbcTemplate.update("INSERT INTO inflacao (taxaInflacao, dataApuracao) VALUES (?, ?)",
                            taxaApuracao, dataApuracao);

                    List<Inflacao> inflacao = jdbcTemplate.query(
                            "SELECT * FROM inflacao ORDER BY id DESC LIMIT 1",
                            new BeanPropertyRowMapper<>(Inflacao.class)
                    );

                    jdbcTemplate.update(
                            "INSERT INTO logInflacao (idInflacao, descricao) VALUES (?, ?)",
                            inflacao.getFirst().getId(),
                            "Os registros " + taxaApuracao + " e " + dataApuracao + " foram inseridos"
                    );

                    count++;

                } catch (Exception e) {
                    System.err.println("Erro na linha " + row.getRowNum() + ": " + e.getMessage());
                }
            }

            System.out.println("[" + timestamp + "] ✅ Inserção de " + count + " registros de inflação concluída!");
        } catch (Exception e) {
            tratarErro(e, timestamp);
        }
    }

    public void inserirDadosSelic(String key) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println("[" + timestamp + "] ⏳ Iniciando leitura do arquivo XLSX: " + key);

        try (InputStream inputStream = baixarArquivo(key);
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            int count = 0;

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                Cell cData = row.getCell(0);
                Cell cValor = row.getCell(1);

                if (cData == null || cValor == null) continue;

                try {
                    String dataApuracao = cData.toString().trim();
                    Double taxaSelic = Double.parseDouble(cValor.toString().replace(",", "."));

                    jdbcTemplate.update("INSERT INTO selic (taxaSelic, dataApuracao) VALUES (?, ?)",
                            taxaSelic, dataApuracao);

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
                    System.err.println("Erro na linha " + row.getRowNum() + ": " + e.getMessage());
                }
            }

            System.out.println("[" + timestamp + "] Inserção de " + count + " registros de SELIC concluída!");
        } catch (Exception e) {
            tratarErro(e, timestamp);
        }
    }
    public void inserirDadosPibConstrucaoCivil(String key) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println("[" + timestamp + "] ⏳ Lendo XLSX: " + key);

        try (InputStream inputStream = baixarArquivo(key);
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            int count = 0;

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // pula cabeçalho

                // Evita null pointer para células ausentes
                Cell cellData = row.getCell(0);
                Cell cellValor = row.getCell(1);

                // pula linhas totalmente vazias
                if (cellData == null && cellValor == null) continue;

                String dataApuracao = cellData != null ? cellData.toString().trim() : "";
                String valorStr = cellValor != null ? cellValor.toString().replace(",", ".") : "";

                if (dataApuracao.isEmpty() || valorStr.isEmpty()) {
                    System.err.println("Linha " + row.getRowNum() + " ignorada (dados vazios)");
                    continue;
                }

                try {
                    Double taxaApuracao = Double.parseDouble(valorStr);

                    System.out.println("Linha " + row.getRowNum() +
                            ": Data=" + dataApuracao +
                            " | Taxa=" + taxaApuracao);

                    jdbcTemplate.update(
                            "INSERT INTO inflacao (taxaInflacao, dataApuracao) VALUES (?, ?)",
                            taxaApuracao, dataApuracao
                    );

                    List<Inflacao> inflacao = jdbcTemplate.query(
                            "SELECT * FROM inflacao ORDER BY id DESC LIMIT 1",
                            new BeanPropertyRowMapper<>(Inflacao.class)
                    );

                    jdbcTemplate.update(
                            "INSERT INTO logInflacao (idInflacao, descricao) VALUES (?, ?)",
                            inflacao.getFirst().getId(),
                            "Os registros " + taxaApuracao + " e " + dataApuracao + " foram inseridos"
                    );

                } catch (NumberFormatException e) {
                    System.err.println("Linha " + row.getRowNum() +
                            " ignorada: valor numérico inválido (" + valorStr + ")");
                }
            }


            System.out.println("[" + timestamp + "] ✅ Inserção de " + count + " PIBs Constr. Civil concluída!");
        } catch (Exception e) {
            tratarErro(e, timestamp);
        }
    }
    public void inserirDadosPib(String key) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println("[" + timestamp + "] ⏳ Lendo XLSX: " + key);

        try (InputStream inputStream = baixarArquivo(key);
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            int count = 0;

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                try {
                    String trimestre = row.getCell(0).toString().replace("�", "º");
                    String ano = row.getCell(1).toString();
                    Double valorPib = Double.parseDouble(row.getCell(14).toString());

                    jdbcTemplate.update("INSERT INTO pib (trimestre, ano, pib) VALUES (?, ?, ?)",
                            trimestre, ano, valorPib);

                    List<Pib> pib = jdbcTemplate.query(
                            "SELECT * FROM pib ORDER BY id DESC LIMIT 1",
                            new BeanPropertyRowMapper<>(Pib.class)
                    );

                    jdbcTemplate.update(
                            "INSERT INTO logPib (idPib, descricao) VALUES (?, ?)",
                            pib.getFirst().getId(),
                            "Registro " + valorPib + " | " + trimestre + " | " + ano + " inserido"
                    );

                    count++;
                } catch (Exception e) {
                    System.err.println("Erro linha " + row.getRowNum() + ": " + e.getMessage());
                }
            }

            System.out.println("[" + timestamp + "] ✅ Inserção de " + count + " registros de PIB concluída!");
        } catch (Exception e) {
            tratarErro(e, timestamp);
        }
    }

    public void inserirDadosPopulacao(String key) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println("[" + timestamp + "] ⏳ Lendo XLSX: " + key);

        try (InputStream inputStream = baixarArquivo(key);
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            int count = 0, total = 0;

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                try {
                    String ano = row.getCell(0).toString();
                    String codigoIbge = row.getCell(1).toString();
                    String municipio = row.getCell(2).toString().trim().toLowerCase();
                    int qtdPopulacao = (int) Double.parseDouble(row.getCell(3).toString());
                    int homens = (int) Double.parseDouble(row.getCell(4).toString());
                    int mulheres = (int) Double.parseDouble(row.getCell(5).toString());
                    double razaoSexo = Double.parseDouble(row.getCell(6).toString().replace(",", "."));
                    double idadeMedia = Double.parseDouble(row.getCell(7).toString().replace(",", "."));
                    double densidadeDemografico = Double.parseDouble(row.getCell(8).toString().replace(",", "."));

                    municipio = Normalizer.normalize(municipio, Normalizer.Form.NFD)
                            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

                    int idZona = getZonaId(municipio);
                    if (idZona == 0) continue;

                    jdbcTemplate.update(
                            "INSERT INTO populacao (ano, codigoIbge, municipio, qtdPopulacao, homens, mulheres, razaoSexo, idadeMedia, densidadeDemografico, idZona) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                            ano, codigoIbge, municipio, qtdPopulacao, homens, mulheres,
                            razaoSexo, idadeMedia, densidadeDemografico, idZona
                    );

                    List<Populacao> pop = jdbcTemplate.query(
                            "SELECT * FROM populacao ORDER BY id DESC LIMIT 1",
                            new BeanPropertyRowMapper<>(Populacao.class)
                    );

                    jdbcTemplate.update(
                            "INSERT INTO logPopulacao (idPopulacao, descricao) VALUES (?, ?)",
                            pop.get(0).getId(),
                            "Registro de população (" + municipio + ") inserido com sucesso."
                    );

                    count++;
                } catch (Exception e) {
                    System.err.println("Erro linha " + row.getRowNum() + ": " + e.getMessage());
                }
                total++;
            }

            System.out.println("[" + timestamp + "] ✅ Inserção de " + count + "/" + total + " registros de população concluída!");
        } catch (Exception e) {
            tratarErro(e, timestamp);
        }
    }

    public void inserirDadosZona(String key) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println("[" + timestamp + "] ⏳ Lendo XLSX: " + key);

        try (InputStream inputStream = baixarArquivo(key);
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            int count = 0;

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                Cell cNome = row.getCell(0);
                if (cNome == null) continue;

                String nome = cNome.toString();
                jdbcTemplate.update("INSERT INTO zona (nome) VALUES (?)", nome);
                count++;
            }

            System.out.println("[" + timestamp + "] ✅ Inserção de " + count + " zonas concluída!");
        } catch (Exception e) {
            tratarErro(e, timestamp);
        }
    }

    private InputStream baixarArquivo(String key) throws IOException {
        System.out.println("📦 Baixando do S3: " + bucketName + "/" + key);
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try {
            ResponseInputStream<GetObjectResponse> response = s3Client.getObject(request);
            System.out.println("✅ Arquivo XLSX carregado com sucesso!");
            return response;
        } catch (S3Exception e) {
            throw new IOException("Erro ao baixar do S3: " + e.awsErrorDetails().errorMessage(), e);
        }
    }

    private void tratarErro(Exception e, String timestamp) {
        if (e instanceof DataAccessException)
            throw new RuntimeException("[" + timestamp + "] Erro no banco: " + e.getMessage(), e);
        else if (e instanceof IOException)
            throw new RuntimeException("[" + timestamp + "] Erro de I/O: " + e.getMessage(), e);
        else
            throw new RuntimeException("[" + timestamp + "] Erro inesperado: " + e.getMessage(), e);
    }

    private int getZonaId(String municipio) {
        List<String> zonaLeste = List.of("sao mateus", "itaquera", "penha", "vila prudente", "cidade tiradentes", "sao miguel paulista", "ermelino matarazzo", "tatuape", "aricanduva", "guilhermina esperanca");
        List<String> zonaSul = List.of("capao redondo", "campo limpo", "jardim angela", "morumbi", "santo amaro", "interlagos", "vila mariana", "vila andrade", "jabaquara", "campo belo");
        List<String> zonaNorte = List.of("santana", "tucuruvi", "casa verde", "freguesia do o", "jacana", "brasilandia", "mandaqui", "tremembe", "vila guilherme", "parada inglesa");
        List<String> zonaOeste = List.of("pinheiros", "lapa", "butanta", "barra funda", "perdizes", "vila leopoldina", "pirituba", "pompeia", "alto da lapa", "sumare");

        if (zonaLeste.contains(municipio)) return 1;
        if (zonaSul.contains(municipio)) return 2;
        if (zonaNorte.contains(municipio)) return 3;
        if (zonaOeste.contains(municipio)) return 4;
        return 0;
    }

    public void fecharS3() {
        s3Client.close();
    }
}
