package presentatie;

import data.BeroepsprofielService;
import data.DataService;
import data.PersoonService;
import data.VakService;
import logica.Beroepsprofiel;
import logica.Student;
import logica.Vak;
import logica.enums.Profiel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * ELOICTSIM; DataEditor
 *
 * @author youke
 * @version 09/06/2022
 */
public class DataEditor {
    private JPanel mainPanel;
    private JTextField voornaamTextField;
    private JTextField achternaamTextField;
    private JLabel voornaamLabel;
    private JTextField inschrijvingsjaarTextField;
    private JList vakkenList;
    private JList<String> studentList;
    private JButton nieuweStudentButton;
    private JButton bewerkStudentButton;
    private JLabel achternaamLabel;
    private JLabel beroepsProfielLabel;
    private JLabel inschrijvingsjaarLabel;
    private JLabel vakkenLabel;
    private JLabel studentenLabel;
    private JComboBox beroepsProfielComboBox;
    private JLabel errorLabel;
    private List<Vak> vakken;
    private List<Student> studenten;
    private Integer id;
    private int[] vakkenId;
    private boolean hasClickedProfiel = false;

    public DataEditor() {
        init();
        String[] studententoArr = getStudentNamen();
        String[] vakkenToArr = getVakkenNamen();
        studentList.setListData(studententoArr);
        vakkenList.setListData(vakkenToArr);
        for (Profiel p: Profiel.values()) {
            beroepsProfielComboBox.addItem(p.toString());
        }

        bewerkStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (
                    voornaamTextField.getText().equals("") ||
                    achternaamTextField.getText().equals("") ||
                    beroepsProfielComboBox.getSelectedIndex() == 0 ||
                    inschrijvingsjaarTextField.getText().equals("")
                ) {
                    appendFields(studententoArr);
                } else if (
                        !voornaamTextField.getText().equals("") &&
                        !achternaamTextField.getText().equals("") &&
                        !inschrijvingsjaarTextField.getText().equals("")
                ) {
                    if (checkNaam(voornaamTextField) && checkNaam(achternaamTextField) && checkDigit(inschrijvingsjaarTextField))
                    errorLabel.setText("alle velden in orde");
                    try {
                        Student student = (Student)PersoonService.findbyNaam(voornaamTextField.getText(), achternaamTextField.getText());
                        Student studentOrigine = (Student)PersoonService.findById(id);
                        if (isDuplicate(student, studentOrigine)) {
                            errorLabel.setForeground(Color.red);
                            errorLabel.setText("er is niets veranderd aan deze student");
                        } else  if (student == null) {
                            errorLabel.setText("er is nog geen student ingesteld");
                        } else {
                            errorLabel.setForeground(Color.black);
                            errorLabel.setText("");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });
        nieuweStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveStudent();
                    JTextField temp = saveStudent();
                    if (temp != null) {
                        errorLabel.setText(temp.getText() + " is fout.");
                    } else {
                        errorLabel.setForeground(Color.green);
                        errorLabel.setText("invoegen gelukt");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        beroepsProfielComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hasClickedProfiel = true;
                try {
                    Profiel profiel = Profiel.get(beroepsProfielComboBox.getSelectedIndex()+1);
                    Beroepsprofiel beroepsprofiel = BeroepsprofielService.findbyId(profiel.getValue()+1);
                    List<Integer> indexList = new ArrayList<>();
                    for (Vak v: beroepsprofiel.getVerplichteVakken()) {
                        indexList.add(v.getId()-1);
                    }
                    int[] indexen = new int[indexList.size()];
                    for (int i = 0; i < indexList.size(); i++) {
                        indexen[i] = indexList.get(i);
                    }
                    vakkenList.setSelectedIndices(indexen);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private JTextField saveStudent() throws SQLException {
        String voornaam = "";
        String achternaam = "";
        Profiel beroepsprofiel;
        Integer inschrijvingsjaar = 0;
        if (checkNaam(voornaamTextField)) {
            voornaam = voornaamTextField.getText();
        } else {
            return voornaamTextField;
        }
        if (checkNaam(achternaamTextField)) {
            achternaam = achternaamTextField.getText();
        } else {
            return achternaamTextField;
        }
        if (checkDigit(inschrijvingsjaarTextField)) {
            inschrijvingsjaar = Integer.parseInt(inschrijvingsjaarTextField.getText());
        } else {
            return inschrijvingsjaarTextField;
        } if (!hasClickedProfiel) {
            return new JTextField("beroepsprofiel");
        }
        beroepsprofiel = Profiel.toEnum(beroepsProfielComboBox.getSelectedItem().toString());
        Student s = new Student(
                PersoonService.getStudenten().get(PersoonService.getStudenten().size()-1).getPersoonId()+1,
                voornaam,
                achternaam,
                beroepsprofiel,
                inschrijvingsjaar
        );
        for (int i : vakkenList.getSelectedIndices()) {
            if (addKeuzeVak(beroepsprofiel, (i+1)) != null) {
                s.addVak(addKeuzeVak(beroepsprofiel, (i+1)));
            }
        }
        PersoonService.saveStudent(s);
        hasClickedProfiel = false;
        return null;
    }

    private Vak addKeuzeVak(Profiel profiel, int vakId) {
        try {
            Vak vak = VakService.findById(vakId);
            Beroepsprofiel b = BeroepsprofielService.findbyId(profiel.getValue()+1);
            boolean isKeuzeVak = true;
            for (Vak v: b.getVerplichteVakken()) {
                if (vak.equals(v)) {
                    isKeuzeVak = false;
                    break;
                }
            }
            if (isKeuzeVak) {
                return vak;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private boolean isDuplicate(Student student, Student studentOrigine) {
        if (studentOrigine.equals(student) && student !=null) {
            if (student.getVakken().size() != studentOrigine.getVakken().size()) {
                List<Boolean> check = new ArrayList<>();
                for (int i = 0; i < student.getVakken().size(); i++) {
                    if (student.getVakken().get(i).equals(studentOrigine.getVakken().get(i))) {
                        check.add(true);
                    } else {
                        check.add(false);
                    }
                }
                if (!check.contains(false)) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private boolean checkDigit(JTextField inschrijvingsjaarTextField) {
        if (
            inschrijvingsjaarTextField.getText() != null &&
            !inschrijvingsjaarTextField.getText().equals("") &&
            inschrijvingsjaarTextField.getText().matches("\\d+") &&
            Integer.parseInt(inschrijvingsjaarTextField.getText()) > 1990 &&
            Integer.parseInt(inschrijvingsjaarTextField.getText()) <= Year.now().getValue()
        ) {
            return true;
        }
        return false;
    }

    private boolean checkNaam(JTextField naamField) {
        if (
            naamField.getText() != null &&
            !naamField.getText().equals("") &&
            naamField.getText().substring(0, 1).toUpperCase().equals(naamField.getText().substring(0, 1)) &&
            naamField.getText().length() >=2
        ) {
            return true;
        }
        return false;
    }

    private void appendFields(String[] studententoArr) {
        int index = studentList.getSelectedIndex();
        String[] split = studententoArr[index].split(" ");
        Student s = null;
        try {
            s = (Student)PersoonService.findbyNaam(split[0], split[1]);
            id = s.getPersoonId();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (s != null) {
            voornaamTextField.setText(s.getVoornaam());
            achternaamTextField.setText(s.getAchternaam());
            beroepsProfielComboBox.setSelectedIndex(s.getBeroepsprofiel().getValue());
            inschrijvingsjaarTextField.setText(String.valueOf(s.getInschrijvingsjaar()));
            List<Integer> vakkenIdList = new ArrayList<>();
            for (Vak v : s.getVakken()) {
                vakkenIdList.add(v.getId());
            }
            vakkenId = new int[vakkenIdList.size()];
            for (int i = 0; i < vakkenIdList.size(); i++) {
                vakkenId[i] = vakkenIdList.get(i);
            }
            vakkenList.setSelectedIndices(vakkenId);
        }
    }

    private void init() {
        try {
            DataService.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String[] getVakkenNamen() {
        vakken = new ArrayList<>();
        try {
            vakken = VakService.getVakken();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        List<String> vakkenNamen = new ArrayList<>();
        for (Vak v: vakken) {
            vakkenNamen.add(
                    new StringBuilder()
                            .append(v.getNaam())
                            .append(" (")
                            .append(v.getAantalStp())
                            .append("STP)")
                            .toString()
            );
        }
        String[] namen = new String[vakkenNamen.size()];
        for (int i = 0; i < vakkenNamen.size(); i++) {
            namen[i] = vakkenNamen.get(i);
        }
        return namen;
    }

    private String[] getStudentNamen() {
        studenten = new ArrayList<>();
        try {
            studenten = PersoonService.getStudenten();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<String> studentenNamen = new ArrayList<>();
        for (Student s: studenten) {
            studentenNamen.add(
                    new StringBuilder()
                            .append(s.getVoornaam().substring(0, 1).toUpperCase())
                            .append(s.getVoornaam().substring(1))
                            .append(" ")
                            .append(s.getAchternaam().substring(0, 1).toUpperCase())
                            .append(s.getAchternaam().substring(1))
                            .toString()
                    );
        }
        String[] toArr = new String[studentenNamen.size()];
        for (int i = 0; i < studentenNamen.size(); i++) {
            toArr[i] = studentenNamen.get(i);
        }
        return toArr;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("DataEditor");
        frame.setContentPane(new DataEditor().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
