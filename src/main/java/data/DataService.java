package data;

import java.sql.SQLException;

/**
 * ELOICTSIM; DataService
 *
 * @author youke
 * @version 07/06/2022
 */
public class DataService {
    /**
     * alle services aanspreken in de juiste volgorde om de chache te vullen
     * @throws SQLException
     */
    public static void init() throws SQLException {
        VakService.getVakken();
        LokaalService.getLokalen();
        DeurService.getDeuren();
        BeroepsprofielService.getBeroepsprofielen();
        PersoonService.getPersons();
        InformatiepuntService.getInformatiepunten();
        //todo: void services, waarden worden niet gebruikt =>> voor GUI wel
    }
}
