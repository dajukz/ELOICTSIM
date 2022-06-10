package presentatie;

import data.DataService;
import data.PersoonService;
import data.VakService;
import logica.Student;
import logica.Vak;
import logica.enums.Profiel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
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
    private JTextField beroepsprofielTextField;
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
    private List<Vak> vakken;
    private List<Student> studenten;

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
                    System.out.println("top");
                    try {
                        Student student = (Student)PersoonService.findbyNaam(voornaamTextField.getText(), achternaamTextField.getText());
                        //todo: eerst vakken uitlezen voordat alles weggeschreven wordt anders inconsistentie
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });
        nieuweStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private boolean checkDigit(JTextField inschrijvingsjaarTextField) {
        if (
            inschrijvingsjaarTextField.getText() != null &&
            !inschrijvingsjaarTextField.getText().equals("") &&
            inschrijvingsjaarTextField.getText().matches("\\d+") &&
            Integer.parseInt(inschrijvingsjaarTextField.getText()) > 1990
        ) {
            return true;
        }
        return false;
    }

    private boolean checkNaam(JTextField naamField) {
        if (
            naamField.getText() != null &&
            !naamField.getText().equals("") &&
            naamField.getText().substring(0, 1).toUpperCase().equals(voornaamTextField.getText().substring(0, 1)) &&
            naamField.getText().length() >=2
        ) {
            return true;
        }
        return false;
    }

    private void appendFields(String[] studententoArr) {
        int index = studentList.getSelectedIndex();
        System.out.println(studententoArr[index]);
        String[] split = studententoArr[index].split(" ");
        Student s = null;
        try {
            s = (Student)PersoonService.findbyNaam(split[0], split[1]);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (s != null) {
            voornaamTextField.setText(s.getVoornaam());
            achternaamTextField.setText(s.getAchternaam());
            beroepsProfielComboBox.setSelectedIndex(s.getBeroepsprofiel().getValue());
            inschrijvingsjaarTextField.setText(String.valueOf(s.getInschrijvingsjaar()));
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
