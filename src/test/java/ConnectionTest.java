import data.BeroepsprofielService;
import data.Datalayer;
import data.PersoonService;
import data.VakService;
import logica.Persoon;
import logica.Vak;

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
            init(con); //initialisatie eerst doen
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

    private static void init(Connection con) throws SQLException {
        VakService.getVakken();
        PersoonService.getPersons();
        //LokaalService.getLokalen() etc, volgorde is belangrijk
    }

        private static void testServices(Connection con) throws SQLException {
        List<Persoon> personen = PersoonService.getPersons();
        System.out.println(personen.size());
        System.out.println(personen);
        List<Vak> vakken = VakService.getVakken();
        System.out.println(vakken);

    }
}