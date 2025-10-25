package school.sptech;

public class Selic {
    private Integer id;
    private Double taxaSelic;
    private String dataApuracao;

    public Selic() {
    }

    public Selic(Integer id, Double taxaSelic, String dataApuracao) {
        this.id = id;
        this.taxaSelic = taxaSelic;
        this.dataApuracao = dataApuracao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getTaxaSelic() {
        return taxaSelic;
    }

    public void setTaxaSelic(Double taxaSelic) {
        this.taxaSelic = taxaSelic;
    }

    public String getDataApuracao() {
        return dataApuracao;
    }

    public void setDataApuracao(String dataApuracao) {
        this.dataApuracao = dataApuracao;
    }

    @Override
    public String toString() {
        return "Selic{" +
                "id=" + id +
                ", taxaSelic=" + taxaSelic +
                ", dataApuracao='" + dataApuracao + '\'' +
                '}';
    }
}
