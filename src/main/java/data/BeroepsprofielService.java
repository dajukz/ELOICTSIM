package data;

import logica.Beroepsprofiel;
import logica.Vak;
import logica.enums.Profiel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BeroepsprofielService {
    private static final String SELECTBEROEPSPROFIEL =
            "SELECT id, naam FROM beroepsprofielen;";
    private static final String SELECTVERPLICHTEVAKKEN =
            "SELECT vak_id, beroepsprofiel_id FROM verplichte_vakken;";
    private  static List<Beroepsprofiel> beroepsprofielen;

    /**
     * Leest alle beroepsprofielen uit eloictsim database.
     * @return Lijst van beroepsprofielen met hun verplichten vakken
     * @throws SQLException
     */
    private static List<Beroepsprofiel> readBeroepsprofielen() throws SQLException {
        List<Beroepsprofiel> beroepsprofielen = new ArrayList<>();
        PreparedStatement profielStatement = null;
        try {
            profielStatement = Datalayer.getInstance().getCon().prepareStatement(SELECTBEROEPSPROFIEL);
            ResultSet profielResult = profielStatement.executeQuery();
            while (profielResult.next()) {
                Integer id = profielResult.getInt("id");
                String naam = profielResult.getString("naam");
                Beroepsprofiel profiel = new Beroepsprofiel(id, Profiel.valueOf(naam));
                beroepsprofielen.add(profiel);
            }
            //todo: alle statements closen, resultset wordt dan autmoatisch gesloten
            addVakken(beroepsprofielen);
            return beroepsprofielen;
        } catch (SQLException sqlException) {
            throw new SQLException("iets fout : " + sqlException);
        } finally {
            if (profielStatement != null) {
                profielStatement.close();
            }
        }
    }

    private static void addVakken(List<Beroepsprofiel> beroepsprofielen) throws SQLException {
        PreparedStatement verplichtStatement = null;
        try {
            verplichtStatement = Datalayer.getInstance().getCon().prepareStatement(SELECTVERPLICHTEVAKKEN);
            ResultSet verplichtVakResult = verplichtStatement.executeQuery();
            while (verplichtVakResult.next()) {
                Integer vakId = verplichtVakResult.getInt("vak_id");
                Integer profielId = verplichtVakResult.getInt("beroepsprofiel_id");
                Beroepsprofiel beroepsprofiel = findbyId(beroepsprofielen, profielId);
                if (beroepsprofiel != null) {
                    Vak vak = VakService.findById(vakId);
                    if (vak != null) {
                        beroepsprofiel.addVak(vak);
                    } else {
                        throw new RuntimeException("unknown vak at id: " + vakId);
                    }
                } else {
                    throw new RuntimeException("unknown beroepsprofiel at id: " + profielId);
                }
            }
        } catch (SQLException sqlException) {
            throw new SQLException("iets fout : " + sqlException);
        } finally {
            if (verplichtStatement != null) {
                verplichtStatement.close();
            }
        }
        //todo: check statement close voor sql connecties openhouden/resource management
    }

    public static List<Beroepsprofiel> getBeroepsprofielen() throws SQLException {
        if (beroepsprofielen==null) {
            beroepsprofielen = readBeroepsprofielen();
        }
        return beroepsprofielen;
    }

    public static Beroepsprofiel findbyId(Integer id) throws SQLException {
        return findbyId(getBeroepsprofielen(), id);
    }

    private static Beroepsprofiel findbyId(List<Beroepsprofiel> beroepsprofielen, Integer id) throws SQLException {
        for (Beroepsprofiel profiel: beroepsprofielen) {
            if (profiel.getProfielId().equals(id)) {
                return profiel;
            }
        }
        return null;
    }
}
