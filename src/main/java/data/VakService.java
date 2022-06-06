package data;

import logica.Persoon;
import logica.Vak;
import logica.enums.Periode;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * ELOICTSIM; VakService
 *
 * @author youke
 * @version 24/05/2022
 */
public class VakService {
    private static final String SELECTVAKKEN =
            "SELECT id, naam, aantal_studiepunten, periode, " +
            "vervolgvak_id " +
            "FROM vakken " +
            "LEFT JOIN volgtijdelijkheden " +
            "ON vakken.id = vak_id;";
    private static List<Vak> vakken;

    /**
     * Leest alle vakken uit eloictsim database.
     * @return Lijst van vakken.
     * @throws SQLException
     */
    private static List<Vak> readVakken() throws SQLException {
        List<Vak> vakken = new ArrayList<>();
        Set<Periode> periodeSet = new HashSet<>();
        PreparedStatement statement = Datalayer.getInstance().getCon().prepareStatement(SELECTVAKKEN);
        ResultSet result = statement.executeQuery();
        while(result.next()) {
            Integer id = result.getInt("id");
            Integer vervolgId = result.getInt("vervolgvak_id");
            String naam = result.getString("naam"); // in klasse aanpassen sql is id famn voorn
            Integer aantalStp = result.getInt("aantal_studiepunten");
            String periodes = result.getString("periode");// todo: result.getSet("periode") testen;
            String[] periodeArr = periodes.split(",");
            for (String p: periodeArr) {
                periodeSet.add(Periode.valueOf(p));
            }
            Vak vak = new Vak(id, vervolgId, naam, aantalStp, periodeSet);
            vakken.add(vak);
        }
        return vakken;
    }

    public static List<Vak> getVakken() throws SQLException {
        // bij voorkeur wordt deze lijst ook ge-cache-t.
        if (vakken==null) {
            vakken = readVakken();
        }
        // de lijst van deze instance teruggeven.
        return vakken;
    }

    public static Vak findById(Integer id) throws SQLException {
        for (Vak vak : getVakken()) {
            if (vak.getId().equals(id)) {
                return vak;
            }
        }
        return null;
    }

    public  static Vak findVervolgvak(Vak vak) throws SQLException {
        return findById(vak.getVervolgId());
    }


}
