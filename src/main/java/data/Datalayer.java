package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ELOICTSIM; Datalayer
 *
 * @author youke
 * @version 24/05/2022
 */
public class Datalayer {
    private static Datalayer dl;
    private static String dbName = "eloictsim";
    private final String login = "root";
    private final String pass = "Azerty123";
    private String url;
    private Connection con;

    public static Datalayer getInstance() {
        if (dl == null) {
            dl = new Datalayer(dbName, false);
        }
        return dl;
    }

    public String getDbName() {
        return dbName;
    }

    public  Datalayer(String dbName, boolean alt) {
        this.dbName = dbName;
        if (alt) {
            makeAltConnection();
        } else {
            makeConnection();
        }

    }

    private void makeConnection() {
        try {
            this.con = DriverManager.getConnection(createUrl(), login, pass );
        }catch (SQLException sqlException) {
            Logger.getLogger(Datalayer.class.getName()).log(Level.SEVERE, null, sqlException);
        }
    }

    private void makeAltConnection() {
        try {
            Properties connectionProps = new Properties();
            connectionProps.setProperty("user", this.login);
            connectionProps.setProperty("password", this.pass);
            this.con = DriverManager.getConnection(createUrl(), connectionProps);
        } catch (SQLException sqlException) {
            Logger.getLogger(Datalayer.class.getName()).log(Level.SEVERE, null, sqlException);
        }
    }

    private String createUrl() {
        return new StringBuilder().append("jdbc:mysql://localhost:3306/").append(getDbName()).append("?serverTimezone=UTC&allowMultiQueries=true").toString();
    }

    public Connection getCon() {
        return con;
    }
}
