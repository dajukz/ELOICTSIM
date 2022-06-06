package logica.enums;

import java.util.Locale;

/**
 * Enum van docent-rollen todo: remove :(
 * ELOICTSIM; Rol
 *
 * @author youke
 * @version 24/05/2022
 */
public enum Rol {
    OPLEIDINGSHOOFD,
    TRAJECTBEGELEIDER,
    ANKERPERSOON_INTERNATIONALISERING,
    STAGE_EN_BACHELORPROEFCOORDINATOR;

    /**
     * @return String van docent-rol, null als die rol niet bestaat.
     */
    @Override
    public String toString() {
       switch (this) {
           case OPLEIDINGSHOOFD -> { return "Opleidingshoofd"; }
           case TRAJECTBEGELEIDER -> { return "Trajectbegeleider"; }
           case ANKERPERSOON_INTERNATIONALISERING -> { return "Ankerpersoon Internationalisering"; }
           case STAGE_EN_BACHELORPROEFCOORDINATOR -> { return "Stage- en Bachelorproefcoördinator"; }
           default -> { return null; }
           //alternatief(dynamisch) this.name().toLowercase en dan eerste letter van woord uppercase tenzij regex("en")
           // laatste stuk opzoeken ofzo zie toString beroepsprofiel
       }
    }
}
