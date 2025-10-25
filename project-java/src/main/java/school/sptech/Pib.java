package school.sptech;

public class Pib {
    private Integer id;
    private Integer valor;
    private Integer idZona;

    public Pib() {
    }

    public Pib(Integer id, Integer valor, Integer idZona) {
        this.id = id;
        this.valor = valor;
        this.idZona = idZona;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public Integer getIdZona() {
        return idZona;
    }

    public void setIdZona(Integer idZona) {
        this.idZona = idZona;
    }

    @Override
    public String toString() {
        return "Pib{" +
                "id=" + id +
                ", valor=" + valor +
                ", idZona=" + idZona +
                '}';
    }
}
