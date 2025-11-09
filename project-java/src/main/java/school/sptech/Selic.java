package school.sptech;

public class Selic {
    private Integer idtblSelic;
    private Double taxaSelic;
    private String dataApuracao;

    public Integer getIdtblSelic() {
        return idtblSelic;
    }

    public Double getTaxaSelic() {
        return taxaSelic;
    }

    public String getDataApuracao() {
        return dataApuracao;
    }


    @Override
    public String toString() {
        return "Selic{" +
                "id=" + idtblSelic +
                ", taxaSelic=" + taxaSelic +
                ", dataApuracao='" + dataApuracao + '\'' +
                '}';
    }
}
