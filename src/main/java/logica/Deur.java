package logica;

/**
 * ELOICTSIM; Deur
 *
 * @author youke
 * @version 07/06/2022
 */
public class Deur {
    private Integer id;
    private Integer x1;
    private Integer x2;
    private Integer y1;
    private Integer y2;


    public Deur(Integer id, Integer x1, Integer x2, Integer y1, Integer y2) {
        this.id = id;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public Integer getX1() {
        return x1;
    }

    public Integer getX2() {
        return x2;
    }

    public Integer getY1() {
        return y1;
    }

    public Integer getY2() {
        return y2;
    }

    @Override
    public String toString() {
        return "Deur{" +
                "id=" + id +
                ", x1=" + x1 +
                ", x2=" + x2 +
                ", y1=" + y1 +
                ", y2=" + y2 +
                '}';
    }
}
