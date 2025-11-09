package school.sptech;

public class Inflacao {
    private Integer idtblInflacao;
    private Double valorTaxa;
    private String dtApuracao;

    public Integer getIdtblInflacao() {
        return idtblInflacao;
    }

    public void setIdtblInflacao(Integer idtblInflacao) {
        this.idtblInflacao = idtblInflacao;
    }

    public Double getValorTaxa() {
        return valorTaxa;
    }

    public void setValorTaxa(Double valorTaxa) {
        this.valorTaxa = valorTaxa;
    }

    public String getDtApuracao() {
        return dtApuracao;
    }

    public void setDtApuracao(String dtApuracao) {
        this.dtApuracao = dtApuracao;
    }

    @Override
    public String toString() {
        return "Inflacao{" +
                "idtblInflacao=" + idtblInflacao +
                ", valorTaxa=" + valorTaxa +
                ", dtApuracao='" + dtApuracao + '\'' +
                '}';
    }
}
