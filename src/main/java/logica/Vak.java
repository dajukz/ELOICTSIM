package logica;

import logica.enums.Periode;

import java.util.Set;

/**
 * ELOICTSIM; Vak
 *
 * @author youke
 * @version 24/05/2022
 */
public class Vak {
    private Integer id;
    private Integer vervolgId;
    private String naam;
    private Integer aantalStp;
    private Set<Periode> periode;

    public Vak(Integer id, Integer vervolgId, String naam, Integer aantalStp, Set<Periode> periode) {
        this.id = id;
        this.vervolgId = vervolgId;
        this.naam = naam;
        this.aantalStp = aantalStp;
        this.periode = periode;
    }

    public Integer getId() {
        return id;
    }

    public Integer getVervolgId() {
        return vervolgId;
    }

    public String getNaam() {
        return naam;
    }

    public Integer getAantalStp() {
        return aantalStp;
    }

    public Set<Periode> getPeriode() {
        return periode;
    }

    @Override
    public String toString() {
        return "Vak{" +
                "id=" + id +
                ", vervolgId=" + vervolgId +
                ", naam='" + naam + '\'' +
                ", aantalStp=" + aantalStp +
                ", periode=" + periode +
                '}';
    }
}
