package logica.enums;

/**
 * ELOICTSIM; Periode
 *
 * @author youke
 * @version 30/05/2022
 */
public enum Periode {
    SEM1,
    SEM2;

    @Override
    public String toString() {
        switch (this) {
            case SEM1 -> { return "Semester 1"; }
            case SEM2 -> { return "Semester 2"; }
            default -> { return null; }
        }
    }
}
