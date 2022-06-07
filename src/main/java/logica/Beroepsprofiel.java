package logica;

import logica.enums.Profiel;

import java.util.ArrayList;
import java.util.List;

/**
 * ELOICTSIM; Beroepsprofiel
 *
 * @author youke
 * @version 30/05/2022
 */
public class Beroepsprofiel {
    //todo: beroepsprofiel maken met verplichte vakken, (beroepsprofiel)binden aan student
    private Integer id;
    private Profiel naam;
    private List<Vak> verplichteVakken = new ArrayList<>();

    public Beroepsprofiel(Integer id, Profiel naam) {
        this.id = id;
        this.naam = naam;
    }

    public void addVak(Vak vak) {
        verplichteVakken.add(vak);
    }

    public List<Vak> getVerplichteVakken() {
        return verplichteVakken;
    }

    public Profiel getNaam() {
        return naam;
    }

    public Integer getProfielId() {
        return id;
    }


    @Override
    public String toString() {
        return "Beroepsprofiel{" +
                "id=" + id +
                ", naam=" + naam +
                ", verplichteVakken=" + verplichteVakken +
                '}';
    }
}
