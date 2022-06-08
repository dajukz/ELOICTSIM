package data;

import logica.Deur;

import javax.xml.crypto.Data;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ELOICTSIM; DeurService
 *
 * @author youke
 * @version 08/06/2022
 */
public class DeurService {
    //todo:deeurService queries
    private static final String SELECTDEUREN = "SELECT id, x1, x2, y1, y2 FROM deuren;";
    private  static List<Deur> deuren;

    /**
     * Leest alle Deuren uit eloictsim database.
     * @return Lijst van informatiepunten
     * @throws SQLException
     */
    private static List<Deur> readDeuren() throws SQLException {
        List<Deur> deuren = new ArrayList<>();
        PreparedStatement deurenStatement = null;
        try {
            deurenStatement = Datalayer.getInstance().getCon().prepareStatement(SELECTDEUREN);
            ResultSet deurenResult = deurenStatement.executeQuery();
            while (deurenResult.next()) {
                Integer id = deurenResult.getInt("id");
                Integer x1 = deurenResult.getInt("x1");
                Integer x2 = deurenResult.getInt("x2");
                Integer y1 = deurenResult.getInt("y1");
                Integer y2 = deurenResult.getInt("y2");
                deuren.add(new Deur(id, x1, x2, y1, y2));
            }
        } catch (SQLException sqlException) {
            throw new SQLException("iets fout : " + sqlException);
        }
        return deuren;
    }

    public static List<Deur> getDeuren() throws SQLException {
        if (deuren == null) {
            deuren = readDeuren();
        }
        return deuren;
    }
}
