package data;

import logica.Informatiepunt;
import logica.Lokaal;
import logica.Persoon;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InformatiepuntService {
    private static final String SELECTINFORMATIEPUNTEN =
            "SELECT id, lokaal_id, persoon_id, x, y, beschrijving FROM informatiepunten";
    private static List<Informatiepunt> informatiepunten;

    /**
     * Leest alle Informatiepunten uit eloictsim database.
     * @return Lijst van informatiepunten
     * @throws SQLException
     */
    private static List<Informatiepunt> readInformatiepunten() throws SQLException {
        List<Informatiepunt> informatiepunten = new ArrayList<>();
        PreparedStatement informatiepuntStatement = null;
        try {
            informatiepuntStatement = Datalayer.getInstance().getCon().prepareStatement(SELECTINFORMATIEPUNTEN);
            ResultSet informatieResult = informatiepuntStatement.executeQuery();
            while (informatieResult.next()) {
                Integer id = informatieResult.getInt("id");
                Integer lokaalId = informatieResult.getInt("lokaal_id");
                Integer persoonId = informatieResult.getInt("persoon_id");
                Integer x = informatieResult.getInt("x");
                Integer y = informatieResult.getInt("y");
                String beschrijving = informatieResult.getString("beschrijving");
                Informatiepunt informatiepunt = null;
                if (!lokaalId.equals(0)) {
                    Lokaal lokaal = LokaalService.findById(lokaalId);
                    informatiepunt = new Informatiepunt(id, lokaal, x, y, beschrijving);
                } else if (!persoonId.equals(0)) {
                    Persoon persoon = PersoonService.findById(persoonId);
                    informatiepunt = new Informatiepunt(id, persoon, x, y, beschrijving);
                }
                informatiepunten.add(informatiepunt);
            }
        } catch (SQLException sqlException) {
            throw new SQLException("iets fout : " + sqlException);
        } finally {
            if (informatiepuntStatement != null) {
                informatiepuntStatement.close();
            }
        }
        return informatiepunten;
    }

    public static List<Informatiepunt> getInformatiepunten() throws SQLException {
        if (informatiepunten == null) {
            informatiepunten = readInformatiepunten();
        }
        return informatiepunten;
    }

    public static Informatiepunt findById(Integer id) throws SQLException {
        return findById(getInformatiepunten(), id);
    }

    private static Informatiepunt findById(List<Informatiepunt> informatiepunten, Integer id) {
        for (Informatiepunt p: informatiepunten) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }
}
