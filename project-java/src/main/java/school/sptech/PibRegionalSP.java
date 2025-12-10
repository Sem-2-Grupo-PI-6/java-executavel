package school.sptech;

public class PibRegionalSP {
    private Integer idtblPibRegionalSP;
    private String ano;
    private Double pibSp;

    public Integer getIdtblPibRegionalSP() {
        return idtblPibRegionalSP;
    }

    public void setIdtblPibRegionalSP(Integer idtblPibRegionalSP) {
        this.idtblPibRegionalSP = idtblPibRegionalSP;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public Double getPibSp() {
        return pibSp;
    }

    public void setPibSp(Double pibSp) {
        this.pibSp = pibSp;
    }

    @Override
    public String toString() {
        return "PibRegionalSP{" +
                "idtblPibRegionalSP=" + idtblPibRegionalSP +
                ", ano='" + ano + '\'' +
                ", pibSp=" + pibSp +
                '}';
    }
}
