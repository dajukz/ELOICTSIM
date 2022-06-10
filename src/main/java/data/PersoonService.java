package data;

import logica.*;
import logica.enums.Profiel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ELOICTSIM; PersoonService
 *
 * @author youke
 * @version 24/05/2022
 */
public class PersoonService {
    private static final String SELECTPERSON =
            "SELECT P.id AS id, familienaam, voornaam, " +
                    "studenten.id AS student_id, studenten.beroepsprofiel_id AS beroepsprofiel_id, studenten.inschrijvingsjaar AS inschrijvingsjaar, " +
                    "docenten.id AS docent_id, docenten.rol AS rol " +
                    "FROM personen AS P " +
                    "LEFT JOIN docenten " +
                    "ON P.id = docenten.id " +
                    "LEFT JOIN studenten " +
                    "ON P.id = studenten.id";
    private static final String SELECTSTUDENTVAKKEN = "SELECT student_id, vak_id FROM keuzevakken";
    private static final String SELECTDOCENTVAKKEN = "SELECT docent_id, vak_id FROM docenten_has_vakken;";
    private static List<Persoon> persons;

    /**
     * Leest alle personen uit eloictsim database.
     * @return Lijst van personen
     * @throws SQLException
     */
    private static List<Persoon> readPersonen() throws SQLException {
        List<Persoon> persons = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            statement = Datalayer.getInstance().getCon().prepareStatement(SELECTPERSON);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                Integer id = result.getInt("id");
                String voornaam = result.getString("voornaam"); // in klasse aanpassen sql is id famn voorn
                String familienaam = result.getString("familienaam");
                Integer studentId = result.getInt("student_id");
                Integer docentId = result.getInt("docent_id");
                Persoon person = null;
                if (!studentId.equals(0)) {
                    Integer beroepsprofielId = result.getInt("beroepsprofiel_id");
                    Profiel beroepsprofiel = Profiel.get(beroepsprofielId);
                    Integer inschrijvingsjaar = result.getInt("inschrijvingsjaar");
                    person = new Student(id, voornaam, familienaam, beroepsprofiel, inschrijvingsjaar);
                } else if (!docentId.equals(0)) {
                    String rol = result.getString("rol");
                    person = new Docent(id, voornaam, familienaam, rol);
                }
                if (person != null) {
                    persons.add(person);
                }
            }
            addStudenten(persons);
            addDocenten(persons);
            return persons;
        } catch (SQLException sqlException) {
            throw new SQLException("iets fout : " + sqlException);
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public static void saveStudent(Student student) throws  SQLException {
        Connection con = Datalayer.getInstance().getCon();
        boolean autoCommit = false;
        try {
            String insertPerson = "INSERT INTO personen (familienaam, naam) values (?, ?);";
            PreparedStatement insertPersonenStatement = con.prepareStatement(insertPerson);
            insertPersonenStatement.setString(1, student.getVoornaam());
            insertPersonenStatement.setString(2, student.getAchternaam());
            String insertStudent = "INSERT INTO student (id, beroepsprofiel, inschrijvingsjaar) VALUES ((SELECT MAX(id) FROM personen), ?, ?);";
            PreparedStatement insertStudentStatement = con.prepareStatement(insertStudent);
            insertStudentStatement.setInt(1, student.getBeroepsprofiel().getValue());
            String insertKeuzeVakken = "INSERT INTO keuzevakken (student_id, vak_id) VALUES ( ?, ?)";
            PreparedStatement insertKeuzeVakkenStatement = con.prepareStatement(insertKeuzeVakken);
            autoCommit = con.getAutoCommit();
            con.setAutoCommit(false);
            for (Beroepsprofiel b : BeroepsprofielService.getBeroepsprofielen()) {
                boolean[] check = new boolean[student.getVakken().size()];
                if (b.getNaam().equals(student.getBeroepsprofiel())) {
                    for (int i = 0; i < student.getVakken().size(); i++) {
                        for (int j = 0; j < b.getVerplichteVakken().size(); j++) {
                            if (!student.getVakken().get(i).equals(b.getVerplichteVakken().get(j))) {
                                check[i] = true;
                            } else {
                                check[i] = false;
                            }
                        }
                    }
                }
                for (int i = 0; i < check.length; i++) {
                    if (check[i]) {
                        insertKeuzeVakkenStatement.setInt(1, student.getPersoonId());
                        insertKeuzeVakkenStatement.setInt(2, student.getVakken().get(i).getId());
                        insertKeuzeVakkenStatement.executeUpdate();
                    }
                }
            }
            insertPersonenStatement.executeUpdate();
            insertStudentStatement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("fout" + student.toString());
            System.out.println(sqlException);
            con.rollback();
        } finally {
            con.setAutoCommit(autoCommit);
        }
    }

    private static void updateStudent() throws SQLException {
        Connection con = Datalayer.getInstance().getCon();
        String updatePersoon = "UPDATE personen SET familienaam=?, voornaam=? WHERE id=?;";
        String updateStudent = "UPDATE studenten SET beroepsprofiel_id=?, inschrijvingsjaar=? WHERE id=(SELECT id FROM personen WHERE id=?);";
        String updateKeuzevakken = "UPDATE keuzevakken SET ;"; //todo
        PreparedStatement stmt = con.prepareStatement(updateStudent);
        boolean autoCommit = con.getAutoCommit();
        try {
            con.setAutoCommit(false);
            stmt.executeQuery();
        } catch (SQLException sqlException) {
            con.rollback();
        } finally {
            con.setAutoCommit(autoCommit);
        }

    }

    private static void addStudenten(List<Persoon> persons) throws SQLException {
        PreparedStatement studentVakStatement = null;
        try {
            studentVakStatement = Datalayer.getInstance().getCon().prepareStatement(SELECTSTUDENTVAKKEN);
            ResultSet studentVakResult = studentVakStatement.executeQuery();
            while(studentVakResult.next()) {
                Integer studentId = studentVakResult.getInt("student_id");
                Persoon student = findById(persons, studentId);
                if (student != null) {
                    Integer vakId = studentVakResult.getInt("vak_id");
                    Vak vak = VakService.findById(vakId);
                    if (vak != null) {
                        student.addVak(vak);
                    } else {
                        throw new RuntimeException("unknown vak " + vakId);
                    }
                } else {
                    throw new RuntimeException("unknown student " + studentId);
                }
            }
            addVerplichteVakken(persons);
        } catch (SQLException sqlException) {
            throw new SQLException("iets fout : " + sqlException);
        } finally {
            if (studentVakStatement != null) {
                studentVakStatement.close();
            }
        }
        //todo: voor data editor insert preparedstatement
        //todo: bindings insert into .... (?, ?, ?)
        /*
        *insert into person (naam, voornaam) values  ("henk", "a");
        select id from person where naam = "henk" and voornaam = "a";
        insert into studenten (id, beroepsprofiel, inschrijvingsjaar) values ((select id from person where naam = "henk" and voornaam = "a"),  "ICT", 2012);
         */
    }

    private static void addVerplichteVakken(List<Persoon> persons) throws SQLException {
        List<Beroepsprofiel> profielen = BeroepsprofielService.getBeroepsprofielen();
        for (Persoon p: persons) {
            if (p instanceof Student s) {
                Profiel profiel = s.getBeroepsprofiel();
                for (Beroepsprofiel beroepsprofiel: profielen) {
                    if (profiel == beroepsprofiel.getNaam()) {
                        s.addVak(beroepsprofiel.getVerplichteVakken());
                    }
                }
            }
        }
    }

    private static void addDocenten(List<Persoon> persons) throws SQLException {
        PreparedStatement docentVakStatement = null;
        try {
            docentVakStatement = Datalayer.getInstance().getCon().prepareStatement(SELECTDOCENTVAKKEN);
            ResultSet docentVakResult = docentVakStatement.executeQuery();
            while (docentVakResult.next()) {
                Integer docentId = docentVakResult.getInt("docent_id");
                Persoon docent = findById(persons, docentId);
                if (docent != null) {
                    Integer vakId = docentVakResult.getInt("vak_id");
                    Vak vak = VakService.findById(vakId);
                    if (vak != null) {
                        docent.addVak(vak);
                    } else {
                        throw new RuntimeException("unknown vak at id: " + vakId);
                    }
                } else {
                    throw new RuntimeException("unknown docent at id: " + docentId);
                }
            }
        } catch (SQLException sqlException) {
            throw new SQLException("iets fout" + sqlException);
        } finally {
            if (docentVakStatement != null) {
                docentVakStatement.close();
            }
        }
    }

    public static List<Persoon> getPersons() throws SQLException {
        // bij voorkeur wordt deze lijst ook ge-cache-t.
        if (persons==null) {
            persons = readPersonen();
        }
        // de lijst van deze instance teruggeven.
        return persons;
    }
    public static List<Student> getStudenten() throws SQLException {
        List<Persoon> personen = getPersons();
        List<Student> studenten = new ArrayList<>();
        for (Persoon p : personen) {
            if (p instanceof Student s) {
                studenten.add(s);
            }
        }
        return studenten;
    }

    public static Persoon findById(Integer id) throws SQLException {
        return findById(getPersons(), id);
    }

    private static Persoon findById(List<Persoon> persons, Integer id) {
        for (Persoon person : persons) {
            if (person.getPersoonId().equals(id)) {
                return person;
            }
        }
        return null;
    }

    public static Persoon findbyNaam(String voornaam, String achternaam) throws SQLException {
        for (Persoon person : getPersons()) {
            if (person.getVoornaam().equals(voornaam)) {
                return person;
            }
        }
        return null;
    }
}
