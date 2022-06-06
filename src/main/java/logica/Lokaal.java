package logica;

public class Lokaal {
    //todo: datatypes controleren mysql
    private Integer id;
    private String naam;
    private String lokaalcode;
    private Integer x;
    private Integer y;
    private Integer breedte;
    private Integer lengte;

    public Lokaal(Integer id, String naam, String lokaalcode, Integer x, Integer y, Integer breedte, Integer lengte) {
        this.id = id;
        this.naam = naam;
        this.lokaalcode = lokaalcode;
        this.x = x;
        this.y = y;
        this.breedte = breedte;
        this.lengte = lengte;
    }

    public Integer getId() {
        return id;
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
}