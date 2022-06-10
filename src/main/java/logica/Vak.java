package logica;

import logica.enums.Periode;

import java.util.Objects;
import java.util.Set;

/**
 * ELOICTSIM; Vak
 *
 * @author youke
 * @version 24/05/2022
 */
public class Vak {
    private Integer id;
    //private Integer vervolgId; //todo: foute implementatie, meerde volgtijdelijkheden
    private String naam;
    private Integer aantalStp;
    private Set<Periode> periode;

    public Vak(Integer id, String naam, Integer aantalStp, Set<Periode> periode) {
        this.id = id;
        //this.vervolgId = vervolgId;
        this.naam = naam;
        this.aantalStp = aantalStp;
        this.periode = periode;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vak)) return false;
        Vak vak = (Vak) o;
        return Objects.equals(getId(), vak.getId()) && Objects.equals(getNaam(), vak.getNaam()) && Objects.equals(getAantalStp(), vak.getAantalStp()) && Objects.equals(getPeriode(), vak.getPeriode());
    }

    /*public Integer getVervolgId() {
        return vervolgId;
    }*/

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
        return ">" + naam +
                " (" + aantalStp + "STP)";
    }
}
