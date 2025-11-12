package school.sptech;

public class Pib {
   private Integer idPib;
   private String trimestre;
   private String ano;
   private String tblZona_idZona;

    public Integer getIdPib() {
        return idPib;
    }

    public void setIdPib(Integer idPib) {
        this.idPib = idPib;
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

    public String getTblZona_idZona() {
        return tblZona_idZona;
    }

    public void setTblZona_idZona(String tblZona_idZona) {
        this.tblZona_idZona = tblZona_idZona;
    }

    @Override
    public String toString() {
        return "Pib{" +
                "idPib=" + idPib +
                ", trimestre='" + trimestre + '\'' +
                ", ano='" + ano + '\'' +
                ", tblZona_idZona='" + tblZona_idZona + '\'' +
                '}';
    }
}
