package school.sptech;

public class Inflacao {
    private Integer idtblInflacao;
    private Double taxaInflacao;
    private String dataApuracao;

    public Inflacao() {
    }

    public Inflacao(Integer idtblInflacao, Double taxaInflacao, String dataApuracao) {
        this.idtblInflacao = idtblInflacao;
        this.taxaInflacao = taxaInflacao;
        this.dataApuracao = dataApuracao;
    }

    public Integer getId() {
        return idtblInflacao;
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
                "id=" + idtblInflacao +
                ", taxaInflacao=" + taxaInflacao +
                ", dataApuracao='" + dataApuracao + '\'' +
                '}';
    }
}
