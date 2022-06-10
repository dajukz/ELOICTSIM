package logica;

import java.util.ArrayList;
import java.util.List;

public class Lokaal {
    private Integer id;
    private String naam;
    private String lokaalcode;
    private Integer x;
    private Integer y;
    private Integer breedte;
    private Integer lengte;
    private List<Vak> vakken = new ArrayList<>();

    public Lokaal(Integer id, String naam, String lokaalcode, Integer x, Integer y, Integer breedte, Integer lengte) {
        this.id = id;
        this.naam = naam;
        this.lokaalcode = lokaalcode;
        this.x = x;
        this.y = y;
        this.breedte = breedte;
        this.lengte = lengte;
    }

    public void addVak(Vak vak) {
        vakken.add(vak);
    }

    public Integer getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public String getLokaalcode() {
        return lokaalcode;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getBreedte() {
        return breedte;
    }

    public Integer getLengte() {
        return lengte;
    }

    public List<Vak> getVakken() {
        return vakken;
    }

    @Override
    public String toString() {
        return "Lokaal{" +
                "id=" + id +
                ", naam='" + naam + '\'' +
                ", lokaalcode='" + lokaalcode + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", breedte=" + breedte +
                ", lengte=" + lengte +
                ", vakken=" + vakken +
                '}';
    }
}
