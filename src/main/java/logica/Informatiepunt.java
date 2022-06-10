package logica;

/**
 * ELOICTSIM; Informatiepunt
 *
 * @author youke
 * @version 24/05/2022
 */
public class Informatiepunt {
    private Integer id;
    private Lokaal lokaal;
    private Persoon persoon;
    private Integer x;
    private Integer y;
    private String beschrijving;
    private boolean isLokaal;
    private boolean isPersoon;

    public Informatiepunt(Integer id, Lokaal lokaal, Integer x, Integer y, String beschrijving) {
        this.id = id;
        this.lokaal =  lokaal;
        this.x = x;
        this.y = y;
        this.beschrijving = beschrijving;
        this.isLokaal = true;
        this.isPersoon = false;
    }
    public Informatiepunt(Integer id, Persoon persoon, Integer x, Integer y, String beschrijving) {
        this.id = id;
        this.persoon = persoon;
        this.x = x;
        this.y = y;
        this.beschrijving = beschrijving;
        this.isPersoon = true;
        this.isLokaal = false;
    }

    public Integer getId() {
        return id;
    }

    public Lokaal getLokaal() {
        if (this.lokaal ==null) {
            return null;
        }
        return lokaal;
    }

    public boolean isLokaal() {
        return isLokaal;
    }

    public boolean isPersoon() {
        return isPersoon;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Persoon getPersoon() {
        if (this.persoon == null) {
            return null;
        }
        return persoon;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    @Override
    public String toString() {
        if (this.persoon == null) {
            return "Informatiepunt{" +
                    "id=" + id +
                    ", lokaal=" + lokaal +
                    ", x=" + x +
                    ", y=" + y +
                    ", beschrijving='" + beschrijving + '\'' +
                    '}';
        } else if (this.lokaal == null){
            return "Informatiepunt{" +
                    "id=" + id +
                    ", persoon=" + persoon +
                    ", x=" + x +
                    ", y=" + y +
                    ", beschrijving='" + beschrijving + '\'' +
                    '}';
        }
        else return null;
    }
}
