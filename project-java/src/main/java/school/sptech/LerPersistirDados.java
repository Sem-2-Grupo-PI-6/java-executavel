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
    private final String bucketName = "sixtech-s3";
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

                    jdbcTemplate.update("INSERT INTO tblInflacao (valorTaxa, dtApuracao) VALUES (?, ?)",
                            taxaApuracao, dataApuracao);

                    List<Inflacao> inflacao = jdbcTemplate.query(
                            "SELECT * FROM tblInflacao ORDER BY idtblInflacao DESC LIMIT 1;",
                            new BeanPropertyRowMapper<>(Inflacao.class)
                    );

                    jdbcTemplate.update(
                            "INSERT INTO tblLogArquivos (tipoLog, descricao, tblInflacao_idtblInflacao) VALUES (?, ?, ?)",
                            "INFO",
                            "Os registros " + taxaApuracao + " e " + dataApuracao + " foram inseridos na tabela de inflcao",
                            inflacao.getFirst().getIdtblInflacao()
                    );

                    count++;
                    System.out.println("Os dados inseridos: " + taxaApuracao  + "e" + dataApuracao);

                } catch (Exception e) {
                    jdbcTemplate.update(
                            "INSERT INTO tblLogArquivos (tipoLog, descricao) VALUES (?, ?)",
                            "ERROR",
                            "Erro a o tentar e inserir dados na tabela de inflacao, erro: " + e.getMessage()
                    );
                    System.err.println("Erro na linha " + row.getRowNum() + ": " + e.getMessage());
                }
            }
            System.out.println("[" + timestamp + "] Inserção de " + count + " registros de inflação concluída!");
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

                    jdbcTemplate.update("INSERT INTO tblSelic (valorTaxa, dtApuracao) VALUES (?, ?)",
                            taxaSelic, dataApuracao);

                    List<Selic> selic = jdbcTemplate.query(
                            "SELECT * FROM tblSelic ORDER BY idtblSelic DESC LIMIT 1;",
                            new BeanPropertyRowMapper<>(Selic.class)
                    );
                    System.out.println(selic.getFirst().getIdtblSelic());

                    jdbcTemplate.update(
                            "INSERT INTO tblLogArquivos (tipoLog, descricao, tblSelic_idtblSelic) VALUES (?, ?, ?)",
                            "INFO",
                            "Os registros " + taxaSelic + " e " + dataApuracao + " foram inseridos na tabela de selic",
                            selic.getFirst().getIdtblSelic()
                    );

                    count++;
                    System.out.println("Os dados inseridos: " + taxaSelic  + "e" + dataApuracao);

                } catch (Exception e) {
                    jdbcTemplate.update(
                            "INSERT INTO tblLogArquivos (tipoLog, descricao) VALUES (?, ?)",
                            "ERROR",
                            "Erro a o tentar e inserir dados na tabela de selic erro: " + e.getMessage()
                    );
                    System.err.println("Erro na linha " + row.getRowNum() + ": " + e.getMessage());
                }
            }

            System.out.println("[" + timestamp + "] ✅ Inserção de " + count + " registros de SELIC concluída!");
        } catch (Exception e) {
            tratarErro(e, timestamp);
        }
    }

    public void inserirDadosPibRegionalSP(String key) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println("[" + timestamp + "] ⏳ Iniciando leitura do arquivo XLSX: " + key);

        try (InputStream inputStream = baixarArquivo(key);
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            int count = 0;

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                Cell cAno = row.getCell(0);
                Cell cValor = row.getCell(1);

                if (cAno == null || cValor == null) continue;

                try {
                    String ano = cAno.toString().trim();
                    String[] partes = ano.split("\\.");
                    String anoCerto = partes[0];
                    Double pibSp = Double.parseDouble(cValor.toString().replace(",", "."));

                    System.out.println(anoCerto + " " + pibSp);
                    jdbcTemplate.update("INSERT INTO tblPibRegionalSP (ano, pibSP) VALUES (?, ?)",
                            anoCerto, pibSp);

                    List<PibRegionalSP> pib = jdbcTemplate.query(
                            "SELECT * FROM tblPibRegionalSP ORDER BY idtblPibRegionalSP DESC LIMIT 1",
                            new BeanPropertyRowMapper<>(PibRegionalSP.class)
                    );
                    System.out.println(pib.getFirst().getIdtblPibRegionalSP());

                    jdbcTemplate.update(
                            "INSERT INTO tblLogArquivos (tipoLog, descricao, tblPibRegionalSP_idtblPibRegionalSP) VALUES (?, ?, ?)",
                            "INFO",
                            "Os registros " + ano + " | " + pibSp + " foram inseridos na tabela PibRegionalSP",
                            pib.getFirst().getIdtblPibRegionalSP()
                    );

                    count++;
                    System.out.println("Os dados inseridos: " + anoCerto  + "e" + pibSp);

                } catch (Exception e) {
                    jdbcTemplate.update(
                            "INSERT INTO tblLogArquivos (tipoLog, descricao) VALUES (?, ?)",
                            "ERROR",
                            "Erro a o tentar e inserir dados na tabela PibRegionalSP, erro: " + e.getMessage()
                    );
                    System.err.println("Erro na linha " + row.getRowNum() + ": " + e.getMessage());
                }
            }

            System.out.println("[" + timestamp + "] ✅ Inserção de " + count + " registros de SELIC concluída!");
        } catch (Exception e) {
            tratarErro(e, timestamp);
        }
    }

    public void inserirDadosPibSetor(String key) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println("[" + timestamp + "] ⏳ Lendo XLSX: " + key);

        try (InputStream inputStream = baixarArquivo(key);
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(5);
            int count = 0;
            DataFormatter formatter = new DataFormatter();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                try {
                    Cell cellTrimestre = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell cellAno = row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell cellConstrucaoCivil = row.getCell(7, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell cellServicoTotal = row.getCell(8, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

                    String ano = formatter.formatCellValue(cellAno).trim();

                    String trimestre = formatter.formatCellValue(cellTrimestre).trim();

                    String valorConstrucaoCivelTexto = formatter.formatCellValue(cellConstrucaoCivil).trim().replace(",", "");
                    Double valorRealConstrucaoCivil = Double.parseDouble(valorConstrucaoCivelTexto);

                    String valorServicoToalTexto = formatter.formatCellValue(cellServicoTotal).trim().replace(",", "");
                    Double valorRealServicoTotal = Double.parseDouble(valorServicoToalTexto);

                    jdbcTemplate.update(
                            "INSERT INTO tblPibSetor (trimestre, ano, construcaoCivil, servico) VALUES (?, ?, ?, ?)",
                            trimestre,
                            ano,
                            valorRealConstrucaoCivil,
                            valorRealServicoTotal
                    );

                    List<PibSetor> pib = jdbcTemplate.query(
                            "SELECT * FROM tblPibSetor ORDER BY idtblPibSetor DESC LIMIT 1;",
                            new BeanPropertyRowMapper<>(PibSetor.class)
                    );

                    System.out.println(pib.getFirst().getIdtblPibSetor());

                    jdbcTemplate.update(
                            "INSERT INTO tblLogArquivos (tipoLog, descricao, tblPibSetor_idtblPibSetor) VALUES (?, ?, ?)",
                            "INFO",
                            "Os registros " + trimestre + " | " + ano + " | " +  valorRealConstrucaoCivil + " | " + valorServicoToalTexto + " foram inseridos na tabela de pibSetor",
                            pib.getFirst().getIdtblPibSetor()
                    );
                    count++;
                    System.out.println("dados inseridos com sucesso!");

                } catch (Exception e) {
                    jdbcTemplate.update(
                            "INSERT INTO tblLogArquivos (tipoLog, descricao) VALUES (?, ?)",
                            "ERROR",
                            "Erro a o tentar e inserir dados na tabela de pibSetor erro: " + e.getMessage()
                    );
                    System.err.println("Erro linha " + row.getRowNum() + ": " + e.getMessage());
                }
            }

            System.out.println("[" + timestamp + "] ✅ Inserção concluída! Registros inseridos: " + count);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar XLSX: " + e.getMessage(), e);
        }
    }

    public void inserirDadosPib(String key) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println("[" + timestamp + "] ⏳ Lendo XLSX: " + key);

        try (InputStream inputStream = baixarArquivo(key);
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(5);
            int count = 0;
            DataFormatter formatter = new DataFormatter();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                try {
                    Cell cellTrimestre = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell cellAno = row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell cellPib = row.getCell(12, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

                    if (cellTrimestre == null || cellAno == null || cellPib == null) continue;


                    String trimestre = formatter.formatCellValue(cellTrimestre).replace("�", "º");

                    String ano = formatter.formatCellValue(cellAno);

                    String pibStr = formatter.formatCellValue(cellPib).replace(",", "");
                    Double valorPib = Double.parseDouble(pibStr);

                    jdbcTemplate.update("INSERT INTO tblPib (trimestre, ano, pibGeral) VALUES (?, ?, ?)",
                            trimestre, ano, valorPib
                    );

                    List<Pib> pibList = jdbcTemplate.query(
                            "SELECT * FROM tblPib ORDER BY idPib DESC LIMIT 1",
                            new BeanPropertyRowMapper<>(Pib.class)
                    );

                    jdbcTemplate.update(
                            "INSERT INTO tblLogArquivos (tipoLog, descricao, tblPib_idPib) VALUES (?, ?, ?)",
                            "INFO",
                            "Os registros " + ano + ", " + trimestre +" e "+ valorPib+ " foram inseridos na tabela de selic",
                            pibList.getFirst().getIdPib()
                    );

                    count++;
                    System.out.println("dados inseridos, pib: " + valorPib + " | trimestre: " + trimestre + " | ano: " + ano);
                } catch (Exception e) {
                    jdbcTemplate.update(
                            "INSERT INTO tblLogArquivos (tipoLog, descricao) VALUES (?, ?)",
                            "ERROR",
                            "Erro a o tentar e inserir dados na tabela de pib erro: " + e.getMessage()
                    );
                    System.err.println("Erro na linha " + row.getRowNum() + ": " + e.getMessage());
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
                    String[] partes = ano.split("\\.");
                    String anoCerto = partes[0];

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

                    Integer zona = getZonaId(municipio);

                    jdbcTemplate.update(
                            "INSERT INTO tblPopulacao (ano, codigoIbge, municipio, qtdPopulacao, homens, mulheres, razaoSexo, idadeMedia, densidadeDemo, tblZona_idZona) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                            anoCerto, codigoIbge, municipio, qtdPopulacao, homens, mulheres,
                            razaoSexo, idadeMedia, densidadeDemografico, zona
                    );

                    List<Populacao> pop = jdbcTemplate.query(
                            "SELECT * FROM tblPopulacao ORDER BY idtblPopulacao DESC LIMIT 1",
                            new BeanPropertyRowMapper<>(Populacao.class)
                    );

                    jdbcTemplate.update(
                            "INSERT INTO tblLogArquivos (tipoLog, descricao, tblPopulacao_idtblPopulacao) VALUES (?, ?, ?)",
                            "INFO",
                            "Os registros foram inseridos na tabela de populcao",
                            pop.getFirst().getIdtblPopulacao()
                    );
                    count++;
                    System.out.println("dados da tabela populacao inseridos com sucesso");
                } catch (Exception e) {
                    jdbcTemplate.update(
                            "INSERT INTO tblLogArquivos (tipoLog, descricao) VALUES (?, ?)",
                            "ERROR",
                            "Erro a o tentar e inserir dados na tabela de populacao erro: " + e.getMessage()
                    );
                    System.err.println("Erro linha " + row.getRowNum() + ": " + e.getMessage());
                }
                total++;
            }

            System.out.println("[" + timestamp + "] ✅ Inserção de " + count + "/" + total + " registros de população concluída!");
        } catch (Exception e) {
            tratarErro(e, timestamp);
        }
    }

    private InputStream baixarArquivo(String key) throws IOException {
        System.out.println("Baixando do S3: " + bucketName + "/" + key);
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try {
            ResponseInputStream<GetObjectResponse> response = s3Client.getObject(request);
            System.out.println("Arquivo carregado");
            return response;
        } catch (S3Exception e) {
            throw new IOException("erro ao baixar do s3: " + e.awsErrorDetails().errorMessage(), e);
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

    private Integer getZonaId(String municipio) {
        List<String> zonaLeste = List.of(
                "aruja",
                "biritiba-mirim",
                "ferraz de vasconcelos",
                "guararema",
                "guarulhos",
                "itaquaquecetuba",
                "mogi das cruzes",
                "poa",
                "salesopolis",
                "santa isabel",
                "suzano"
        );

        List<String> zonaNorte = List.of(
                "caieiras",
                "cajamar",
                "francisco morato",
                "franco da rocha",
                "mairipora"
        );

        List<String> zonaOeste = List.of(
                "barueri",
                "carapicuiba",
                "itapevi",
                "jandira",
                "osasco",
                "pirapora do bom jesus"
        );

        List<String> zonaSudoeste = List.of(
                "cotia",
                "embu das artes",
                "embu-guaçu",
                "itapecerica da serra",
                "juquitiba",
                "são lourenco da serra",
                "taboao da serra",
                "vargem grande paulista"
        );

        List<String> zonaSul = List.of(
                "diadema",
                "embu das artes",
                "embu-guacu",
                "itapecerica da serra",
                "juquitiba",
                "maua",
                "ribeirao pires",
                "rio grande da serra",
                "santo andre",
                "sao bernardo do campo",
                "sao caetano do sul"
        );


        if(zonaLeste.contains(municipio)){
            System.out.println(" =========> "+ municipio + " pertence a zona leste");
            return 1;
        }
        if (zonaNorte.contains(municipio)) {
            System.out.println(" =========> "+ municipio + " pertence a zona norte");
            return 2;
        }
        if (zonaOeste.contains(municipio)) {
            System.out.println(" =========> "+ municipio + " pertence a zona oeste");
            return 3;
        }
        if (zonaSul.contains(municipio)) {
            System.out.println(" =========> "+ municipio + " pertence a zona sul");
            return 4;
        }

        System.out.println("sem pertencer a zona leste");
        return 5;
    }

    public void fecharS3() {
        s3Client.close();
    }
}
