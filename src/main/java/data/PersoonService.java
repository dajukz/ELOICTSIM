package data;

import logica.*;
import logica.enums.Profiel;

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
    private static List<Persoon> readPersons() throws SQLException {
        List<Persoon> persons = new ArrayList<>();
        PreparedStatement statement = Datalayer.getInstance().getCon().prepareStatement(SELECTPERSON);
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
        //todo: same for docent
        addDocenten(persons);
        return persons;
    }

    private static void addStudenten(List<Persoon> persons) throws SQLException {
        PreparedStatement studentVakStatement = Datalayer.getInstance().getCon().prepareStatement(SELECTSTUDENTVAKKEN);
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
        PreparedStatement docentVakStatement = Datalayer.getInstance().getCon().prepareStatement(SELECTDOCENTVAKKEN);
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
    }

    public static List<Persoon> getPersons() throws SQLException {
        // bij voorkeur wordt deze lijst ook ge-cache-t.
        if (persons==null) {
            persons = readPersons();
        }
        // de lijst van deze instance teruggeven.
        return persons;
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

    public static Persoon findByVoornaam(String voornaam) throws SQLException {
        for (Persoon person : getPersons()) {
            if (person.getVoornaam().equals(voornaam)) {
                return person;
            }
        }
        return null;
    }
}
