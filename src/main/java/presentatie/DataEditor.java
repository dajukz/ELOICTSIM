package presentatie;

import javax.swing.*;

/**
 * ELOICTSIM; DataEditor
 *
 * @author youke
 * @version 09/06/2022
 */
public class DataEditor {
    private JPanel mainPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JLabel voornaamLabel;
    private JTextField textField4;

    public static void main(String[] args) {
        JFrame frame = new JFrame("DataEditor");
        frame.setContentPane(new DataEditor().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
