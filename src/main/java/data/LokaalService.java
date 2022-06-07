package data;

import logica.Lokaal;
import logica.Vak;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LokaalService {
    //todo: lokaal queries
    private static final String SELECTLOKAAL = "SELECT id, naam, lokaalcode, x, y, breedte, lengte FROM lokalen;";
    private static final String SELECTLOKAALVAKKEN = "SELECT vak_id, lokaal_id FROM vakken_has_lokalen;";

    private static List<Lokaal> lokalen;

    /**
     * Leest alle lokalen en hun vakken uit eloictsim database
     * @return Lijst van lokalen
     * @throws SQLException
     */
    private static List<Lokaal> readLokalen() throws SQLException {
        List<Lokaal> lokalen = new ArrayList<>();
        PreparedStatement lokaalStatement = null;
        try {
            lokaalStatement = Datalayer.getInstance().getCon().prepareStatement(SELECTLOKAAL);
            ResultSet lokaalResults = lokaalStatement.executeQuery();
            while (lokaalResults.next()) {
                Integer id = lokaalResults.getInt("id");
                String naam = lokaalResults.getString("naam");
                String lokaalcode = lokaalResults.getString("lokaalcode");
                Integer x = lokaalResults.getInt("x");
                Integer y = lokaalResults.getInt("y");
                Integer breedte = lokaalResults.getInt("breedte");
                Integer lengte = lokaalResults.getInt("lengte");
                Lokaal lokaal = new Lokaal(id, naam, lokaalcode, x, y, breedte, lengte);
                lokalen.add(lokaal);
            }
            readlokaalvakken(lokalen);
            return lokalen;
        } catch (SQLException sqlException) {
            throw new SQLException("iets fout: " + sqlException);
        } finally {
            if (lokaalStatement != null) {
                lokaalStatement.close();
            }
        }
    }

    private static void readlokaalvakken(List<Lokaal> lokalen) throws SQLException {
        List<Vak> vakken = new ArrayList<>();
        PreparedStatement lokaalVakStatement = null;
        try {
            lokaalVakStatement = Datalayer.getInstance().getCon().prepareStatement(SELECTLOKAALVAKKEN);
            ResultSet lokaalVakResult = lokaalVakStatement.executeQuery();
            while (lokaalVakResult.next()) {
                Integer lokaalId = lokaalVakResult.getInt("lokaal_id");
                Lokaal lokaal = findById(lokalen, lokaalId);
                if (lokaal != null) {
                    Integer vakId = lokaalVakResult.getInt("vak_id");
                    Vak vak = VakService.findById(vakId);
                    if (vak != null) {
                        lokaal.addVak(vak);
                    } else {
                        throw new RuntimeException("unknown vak at id : " + vakId);
                    }
                } else {
                    throw new RuntimeException("unknown lokaal at id : " + lokaalId);
                }
            }
        } catch (SQLException sqlException) {
            throw new SQLException("iets fout " + sqlException);
        } finally {
            if (lokaalVakStatement != null) {
                lokaalVakStatement.close();
            }
        }

    }

    public static List<Lokaal> getLokalen() throws SQLException {
        if (lokalen==null) {
            lokalen = readLokalen();
        }
        return lokalen;
    }

    public static Lokaal findById(Integer id) throws SQLException {
        return findById(getLokalen(), id);
    }

    private static Lokaal findById(List<Lokaal> lokalen, Integer id) {
        for (Lokaal lokaal : lokalen) {
            if (lokaal.getId().equals(id)) {
                return lokaal;
            }
        }
        return null;
    }
}
