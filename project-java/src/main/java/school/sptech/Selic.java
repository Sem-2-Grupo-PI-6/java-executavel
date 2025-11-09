package school.sptech;

public class Selic {
    private Integer idtblSelic;
    private Double valorTaxa;
    private String dtApuracao;

    public Integer getIdtblSelic() {
        return idtblSelic;
    }

    public Double getValorTaxa() {
        return valorTaxa;
    }

    public String getDtApuracao() {
        return dtApuracao;
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
