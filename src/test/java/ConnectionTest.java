import data.*;
import logica.*;
import logica.enums.Profiel;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Connection test
 * ELOICTSIM; ConnectionTest
 *
 * @author youke
 * @version 30/05/2022
 */
public class ConnectionTest {
    public static void main(String[] args) {
        try {
            Connection con = Datalayer.getInstance().getCon(); //belangrijk singleton maken van datalayer zodat niet elke keer opnieuw aangemaakt wordt
//            testSQLSelect(con);
            DataService.init(); //initialisatie eerst doen
            testServices(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void testSQLSelect(Connection con) throws SQLException {
        PreparedStatement statement = con.prepareStatement("SELECT id, familienaam, voornaam FROM personen");
//            boolean success = statement.execute();
//            System.out.println("execute stmt = " + success);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getInt("id"));
        }
//            System.out.println(set.getRow());
    }

    private static void testServices(Connection con) throws SQLException {
    List<Persoon> personen = PersoonService.getPersons();
    System.out.println(personen.size());
    System.out.println(personen);
    List<Lokaal> lokalen = LokaalService.getLokalen();
    System.out.println(lokalen);
    List<Deur> deuren = DeurService.getDeuren();
    System.out.println(deuren);
    List<Informatiepunt> informatiepunten = InformatiepuntService.getInformatiepunten();
    System.out.println(informatiepunten);
    List<Beroepsprofiel> beroepsprofielen = BeroepsprofielService.getBeroepsprofielen();
    System.out.println(beroepsprofielen);
    List<Vak> vakken = VakService.getVakken();
    System.out.println(vakken);

    }
}
