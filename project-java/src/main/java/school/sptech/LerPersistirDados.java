
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
                                "Od registros "+ taxaApuracao +" e "+ dataApuracao +" foram inseridos"
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
