package logica.enums;

/**
 * Verschillende beroepsprofielen studenten
 * ELOICTSIM; Beroepsprofiel
 *
 * @author youke
 * @version 29/05/2022
 */
public enum Profiel {
    TELECOMMUNICATIONS_ENGINEER(0),
    INTERNET_OF_THINGS_DEVELOPER(1),
    NETWORK_SECURITY_ENGINEER(2),
    SOFTWARE_AI_DEVELOPER(3),
    WEB_MOBILE_DEVELOPER(4);

    private final int value;

    Profiel(int value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return
                (new StringBuilder(this.name().substring(0, 1))
                .append(this.name().substring(1).toLowerCase())
                .toString())
                .replaceAll("_", " ");
    }

    public static Profiel toEnum(String stringVal) {
        Profiel profiel = null;
        final String telCom = "Telecommunications engineer";
        final String iot = "Internet of things developer";
        final String netSec = "Network security engineer";
        final String softDev = "Software ai developer";
        final String webDev = "Web mobile developer";
        switch (stringVal) {
            case telCom -> profiel = TELECOMMUNICATIONS_ENGINEER;
            case iot -> profiel = INTERNET_OF_THINGS_DEVELOPER;
            case netSec -> profiel = NETWORK_SECURITY_ENGINEER;
            case softDev -> profiel = SOFTWARE_AI_DEVELOPER;
            case webDev -> profiel = WEB_MOBILE_DEVELOPER;
            default -> profiel = null;
        }
        return profiel;
    }

    public static Profiel get(int i) {
        if (i == 0) {
            return null;
        }
        return values()[i-1]; //todo: tijdelijke oplossing
    }

    public int getValue() {
        return value;
    }
}
