package presentatie;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class EloictSimGui {

    //bron: https://huisstijl2.odisee.be/huisstijl/kleurenpalet-en-kleurbalans
    private static final Color SPELER_KLEUR =new Color(231,63,22);//Warmrood
    private static final Color LOKAAL_KLEUR =new Color(31,65,107);//Nachtblauw
    private static final Color DEUR_KLEUR =new Color(211,221,242);//Mistblauw

    private JPanel tekenPanel;
    private JLabel titelLabel;
    private JPanel mainPanel;

    private Image background;
    private Image lokaalKlein, studentKlein, docentKlein;
    private ImageIcon lokaalGroot, studentGroot, docentGroot;


    public EloictSimGui() {

    }

    private void createUIComponents() {
        background = laadAfbeelding("d-gang");
        lokaalKlein = laadAfbeelding("32px/lokaal");
        studentKlein = laadAfbeelding("32px/student");
        docentKlein = laadAfbeelding("32px/docent");
        lokaalGroot = laadIcoon("64px/lokaal");
        studentGroot = laadIcoon("64px/student");
        docentGroot = laadIcoon("64px/docent");

        tekenPanel=new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background,0,0,null);
                Graphics2D g2=(Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                //Voeg hier je tekenwerk toe
            }

            //Het kan nuttig zijn om je tekenwerk op te splitsen en hier methoden toe te voegen om specifieke zaken te tekenen
        };
    }

    private static Image laadAfbeelding(String bestand) {
        try {
            URL resource = EloictSimGui.class.getResource("/" + bestand + ".png");
            return ImageIO.read(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ImageIcon laadIcoon(String bestand) {
        return new ImageIcon(laadAfbeelding(bestand));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ELO-ICT SIM - Youke Thomas");
        frame.setContentPane(new EloictSimGui().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(laadAfbeelding("O-32x32"));
        frame.pack();
        frame.setVisible(true);
        if(frame.getTitle().contains("student"))
            JOptionPane.showMessageDialog(
                    frame,
                    "Zorg dat je naam in de titel van je JFrame staat. \n" +
                            "Vervang hiervoor 'naam student' door je eigen naam in de code",
                    "Pas je naam aan",
                    JOptionPane.WARNING_MESSAGE);
    }
}
