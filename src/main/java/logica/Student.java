package logica;

import logica.enums.Profiel;

import java.util.Objects;

/**
 * ELOICTSIM; Student
 *
 * @author youke
 * @version 24/05/2022
 */
public class Student extends Persoon{
    private Profiel beroepsprofiel;
    private Integer inschrijvingsjaar;

    public Student(Integer id, String voornaam, String achternaam, Profiel beroepsprofiel, Integer inschrijvingsjaar) {
        super(id, voornaam, achternaam);
        this.beroepsprofiel = beroepsprofiel;
        this.inschrijvingsjaar = inschrijvingsjaar;
    }

    public Profiel getBeroepsprofiel() {
        return beroepsprofiel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return beroepsprofiel == student.beroepsprofiel && Objects.equals(inschrijvingsjaar, student.inschrijvingsjaar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beroepsprofiel, inschrijvingsjaar);
    }

    @Override
    public String toString() {
        return "Student{" + super.toString() + ", " +
                "beroepsprofiel=" + beroepsprofiel +
                ", inschrijvingsjaar=" + inschrijvingsjaar +
                '}';
    }
}
