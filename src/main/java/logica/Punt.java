package logica;

import java.awt.*;
import java.util.Objects;

/**
 * ELOICTSIM; Punt
 *
 * @author youke
 * @version 24/05/2022
 */
public class Punt {
    private int x;
    private int y;

    public Punt(int x, int y) {
        if (isXCorrect(x)) {
            this.x = x;
        }
        if (isYCorrect(y)) {
            this.y = y;
        }
    }

    private boolean isXCorrect(int val) {
        if (val >= 0 || val <= Helper.getScreenSize()[0]) {
            return true;
        }
        return false;
    }
    private boolean isYCorrect(int val) {
        if (val >= 0 || val <= Helper.getScreenSize()[1]) {
            return true;
        }
        return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Punt)) return false;
        Punt punt = (Punt) o;
        return getX() == punt.getX() && getY() == punt.getY();
    }
}
