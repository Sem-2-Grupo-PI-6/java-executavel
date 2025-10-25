package school.sptech;

public class Zona {
    private Integer id;
    private String nome;

    public Zona() {
    }

    public Zona(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Zona{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
