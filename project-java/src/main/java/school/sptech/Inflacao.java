package school.sptech;

public class Inflacao {
    private Integer id;
    private Double taxaInflacao;
    private String dataApuracao;

    public Inflacao() {
    }

    public Inflacao(Integer id, Double taxaInflacao, String dataApuracao) {
        this.id = id;
        this.taxaInflacao = taxaInflacao;
        this.dataApuracao = dataApuracao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getTaxaInflacao() {
        return taxaInflacao;
    }

    public void setTaxaInflacao(Double taxaInflacao) {
        this.taxaInflacao = taxaInflacao;
    }

    public String getDataApuracao() {
        return dataApuracao;
    }

    public void setDataApuracao(String dataApuracao) {
        this.dataApuracao = dataApuracao;
    }

    @Override
    public String toString() {
        return "Inflacao{" +
                "id=" + id +
                ", taxaInflacao=" + taxaInflacao +
                ", dataApuracao='" + dataApuracao + '\'' +
                '}';
    }
}
