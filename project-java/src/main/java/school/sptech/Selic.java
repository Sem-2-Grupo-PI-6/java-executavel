package school.sptech;

public class Selic {
    private Integer idtblSelic;
    private Double valorTaxa;
    private String dtApuracao;

    public Integer getIdtblSelic() {
        return idtblSelic;
    }

    public void setIdtblSelic(Integer idtblSelic) {
        this.idtblSelic = idtblSelic;
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
        return "Selic{" +
                "idtblSelic=" + idtblSelic +
                ", valorTaxa=" + valorTaxa +
                ", dtApuracao='" + dtApuracao + '\'' +
                '}';
    }
}
