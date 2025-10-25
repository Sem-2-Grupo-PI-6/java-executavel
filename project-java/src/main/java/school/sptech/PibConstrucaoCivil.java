package school.sptech;

public class PibConstrucaoCivil {
    private Integer id;
    private Double valorPib;
    private String dataApuracao;

    public PibConstrucaoCivil() {
    }

    public PibConstrucaoCivil(Integer id, Double valorPib, String dataApuracao) {
        this.id = id;
        this.valorPib = valorPib;
        this.dataApuracao = dataApuracao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getValorPib() {
        return valorPib;
    }

    public void setValorPib(Double valorPib) {
        this.valorPib = valorPib;
    }

    public String getDataApuracao() {
        return dataApuracao;
    }

    public void setDataApuracao(String dataApuracao) {
        this.dataApuracao = dataApuracao;
    }

    @Override
    public String toString() {
        return "PibConstrucaoCivil{" +
                "id=" + id +
                ", valorPib=" + valorPib +
                ", dataApuracao='" + dataApuracao + '\'' +
                '}';
    }
}
