package school.sptech;

public class Populacao {
    private Integer id;
    private String ano;
    private String codigoIbge;
    private String municipio;
    private Integer qtdPopulacao;
    private Integer homens;
    private Integer mulheres;
    private Double razaoSexo;
    private Double idadeMedia;
    private Double densidadeDemografico;
    private Integer idZona;

    public Populacao() {
    }

    public Populacao(Integer id, String ano, String codigoIbge, String municipio,
                     Integer qtdPopulacao, Integer homens, Integer mulheres,
                     Double razaoSexo, Double idadeMedia, Double densidadeDemografico, Integer idZona) {
        this.id = id;
        this.ano = ano;
        this.codigoIbge = codigoIbge;
        this.municipio = municipio;
        this.qtdPopulacao = qtdPopulacao;
        this.homens = homens;
        this.mulheres = mulheres;
        this.razaoSexo = razaoSexo;
        this.idadeMedia = idadeMedia;
        this.densidadeDemografico = densidadeDemografico;
        this.idZona = idZona;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getCodigoIbge() {
        return codigoIbge;
    }

    public void setCodigoIbge(String codigoIbge) {
        this.codigoIbge = codigoIbge;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public Integer getQtdPopulacao() {
        return qtdPopulacao;
    }

    public void setQtdPopulacao(Integer qtdPopulacao) {
        this.qtdPopulacao = qtdPopulacao;
    }

    public Integer getHomens() {
        return homens;
    }

    public void setHomens(Integer homens) {
        this.homens = homens;
    }

    public Integer getMulheres() {
        return mulheres;
    }

    public void setMulheres(Integer mulheres) {
        this.mulheres = mulheres;
    }

    public Double getRazaoSexo() {
        return razaoSexo;
    }

    public void setRazaoSexo(Double razaoSexo) {
        this.razaoSexo = razaoSexo;
    }

    public Double getIdadeMedia() {
        return idadeMedia;
    }

    public void setIdadeMedia(Double idadeMedia) {
        this.idadeMedia = idadeMedia;
    }

    public Double getDensidadeDemografico() {
        return densidadeDemografico;
    }

    public void setDensidadeDemografico(Double densidadeDemografico) {
        this.densidadeDemografico = densidadeDemografico;
    }

    public Integer getIdZona() {
        return idZona;
    }

    public void setIdZona(Integer idZona) {
        this.idZona = idZona;
    }

    @Override
    public String toString() {
        return "Populacao{" +
                "id=" + id +
                ", ano='" + ano + '\'' +
                ", codigoIbge='" + codigoIbge + '\'' +
                ", municipio='" + municipio + '\'' +
                ", qtdPopulacao=" + qtdPopulacao +
                ", homens=" + homens +
                ", mulheres=" + mulheres +
                ", razaoSexo=" + razaoSexo +
                ", idadeMedia=" + idadeMedia +
                ", densidadeDemografico=" + densidadeDemografico +
                ", idZona=" + idZona +
                '}';
    }
}
