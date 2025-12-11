package school.sptech;

public enum enumZona {
    ZONA_NORTE(1),
    ZONA_LESTE(2),
    ZONA_SUL(3),
    ZONA_OESTE(4),
    LITORAL(5);

        private Integer idZona;

    enumZona(Integer idZona) {
        this.idZona = idZona;
    }

    public Integer getIdZona() {
        return idZona;
    }
}
