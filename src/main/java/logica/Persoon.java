package logica;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ELOICTSIM; Persoon
 *
 * @author youke
 * @version 24/05/2022
 */
public class Persoon {
    private Integer persoonId;
    private String voornaam;
    private String achternaam;
    private List<Vak> vakken = new ArrayList<>();

    public Persoon(Integer id, String voornaam, String achternaam) {
        this.persoonId = id;
        this.voornaam = voornaam;
        this.achternaam = achternaam;
    }

    public void addVak(Vak vak) {
        vakken.add(vak);
    }
    public void addVak(List<Vak> vak) {
        vakken.addAll(vak);
    }

    public Integer getPersoonId() {
        return persoonId;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public List<Vak> getVakken() {
        return vakken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Persoon)) return false;
        Persoon persoon = (Persoon) o;
        return Objects.equals(getPersoonId(), persoon.getPersoonId()) && Objects.equals(getVoornaam(), persoon.getVoornaam()) && Objects.equals(getAchternaam(), persoon.getAchternaam()) && Objects.equals(getVakken(), persoon.getVakken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPersoonId(), getVoornaam(), getAchternaam(), getVakken());
    }

    @Override
    public String toString() {
        return
                "Naam= " + voornaam + " " + achternaam + '\n';
    }
}
