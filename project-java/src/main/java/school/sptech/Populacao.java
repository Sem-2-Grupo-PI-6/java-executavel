package school.sptech;

public class Populacao {
    private Integer idtblPopulacao;
    private String ano;
    private String codigoIbge;
    private String municipio;
    private Integer qtdPopulacao;
    private Integer homens;
    private Integer mulheres;
    private Double razaoSexo;
    private Double idadeMedia;
    private Double densidadeDemo;
    private Integer idZona;

    public Integer getIdtblPopulacao() {
        return idtblPopulacao;
    }

    public void setIdtblPopulacao(Integer idtblPopulacao) {
        this.idtblPopulacao = idtblPopulacao;
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

    public Double getDensidadeDemo() {
        return densidadeDemo;
    }

    public void setDensidadeDemo(Double densidadeDemo) {
        this.densidadeDemo = densidadeDemo;
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
                "idtblPopulacao=" + idtblPopulacao +
                ", ano='" + ano + '\'' +
                ", codigoIbge='" + codigoIbge + '\'' +
                ", municipio='" + municipio + '\'' +
                ", qtdPopulacao=" + qtdPopulacao +
                ", homens=" + homens +
                ", mulheres=" + mulheres +
                ", razaoSexo=" + razaoSexo +
                ", idadeMedia=" + idadeMedia +
                ", densidadeDemo=" + densidadeDemo +
                ", idZona=" + idZona +
                '}';
    }
}
