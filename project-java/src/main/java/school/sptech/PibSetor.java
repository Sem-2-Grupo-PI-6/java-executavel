package school.sptech;

public class PibSetor {
    private Double idtblPibSetor;
    private String trimestre;
    private String ano;
    private Double construcaoCivil;
    private Double servico;

    public Double getIdtblPibSetor() {
        return idtblPibSetor;
    }

    public void setIdtblPibSetor(Double idtblPibSetor) {
        this.idtblPibSetor = idtblPibSetor;
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

    public Double getConstrucaoCivil() {
        return construcaoCivil;
    }

    public void setConstrucaoCivil(Double construcaoCivil) {
        this.construcaoCivil = construcaoCivil;
    }

    public Double getServico() {
        return servico;
    }

    public void setServico(Double servico) {
        this.servico = servico;
    }

    @Override
    public String toString() {
        return "PibSetor{" +
                "idtblPibSetor=" + idtblPibSetor +
                ", trimestre='" + trimestre + '\'' +
                ", ano='" + ano + '\'' +
                ", construcaoCivil=" + construcaoCivil +
                ", servico=" + servico +
                '}';
    }
}
