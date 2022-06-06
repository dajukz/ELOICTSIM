package logica;

/**
 * ELOICTSIM; Informatiepunt
 *
 * @author youke
 * @version 24/05/2022
 */
public class Informatiepunt {
    private Integer id;
    private Integer lokaal_id;
    private Integer persoon_id;
    private String beschrijving;
    private Punt punt;
    private Persoon persoonsInfo;

    /*
    informatiepunt kan ofwel een bordje zijn met info erop ofwel een persoon
    elk object eigen constructor
     */
    public Informatiepunt(int id, int lokaal_id, Punt punt, String beschrijving) {
        if (id != lokaal_id && id > 0) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("onjuist id");
        }
        if (id != lokaal_id && lokaal_id > 0) {
            this.lokaal_id = lokaal_id;
        } else {
            throw new IllegalArgumentException("onjuist lokaal_id");
        }
        if (Helper.isValidPoint(punt)) {
            this.punt = punt;
        } else {
            throw new IllegalArgumentException("De coordinaten van het punt zijn fout");
        }
        if (!beschrijving.equals("") || beschrijving != null) {
            this.beschrijving = beschrijving;
        } else {
            throw new IllegalArgumentException("Er is geeen beschrijving aanwezig");
        }
        //alle objecten aanroepen in gui naar getters, persoon is geen informatiepunt
        //persoon heeft een informatiepunt, aggregatie?
    }
    public Informatiepunt(int id, int persoon_id, Punt punt, String beschrijving, Persoon persoonsInfo) {
        if (id != persoon_id && id > 0) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("onjuist id");
        }
        if (id != persoon_id && persoon_id > 0) {
            this.persoon_id = lokaal_id;
        } else {
            throw new IllegalArgumentException("onjuist lokaal_id");
        }
        if (Helper.isValidPoint(punt)) {
            this.punt = punt;
        } else {
            throw new IllegalArgumentException("De coordinaten van het punt zijn fout");
        }
        if (!beschrijving.equals("") || beschrijving != null) {
            this.beschrijving = beschrijving;
        } else {
            throw new IllegalArgumentException("Er is geeen beschrijving aanwezig");
        }
        this.persoonsInfo = persoonsInfo; //controle uitvoeren in persoon, bij punt ook bekijken
    }

    protected int getId() {
        return id;
    }

    public Integer getLokaal_id() {
        return lokaal_id;
    }

    public Integer getPersoon_id() {
        return persoon_id;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public Punt getPunt() {
        return punt;
    }

    public Persoon getPersoonsInfo() {
        return persoonsInfo;
    }
}
