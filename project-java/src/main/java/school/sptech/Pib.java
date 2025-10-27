package school.sptech;

public class Pib {
    private Integer id;
    private String trimestre;
    private String ano;
    private Double pib;

    public Pib() {
    }

    public Pib(Integer id, String trimestre, String ano, Double pib) {
        this.id = id;
        this.trimestre = trimestre;
        this.ano = ano;
        this.pib = pib;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTrimestre() {
        return trimestre;
    }

    public void setTrimestre(String trimestre) {
        this.trimestre = trimestre;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public Double getPib() {
        return pib;
    }

    public void setPib(Double pib) {
        this.pib = pib;
    }

    @Override
    public String toString() {
        return "Pib{" +
                "id=" + id +
                ", trimestre='" + trimestre + '\'' +
                ", ano='" + ano + '\'' +
                ", pib=" + pib +
                '}';
    }
}
