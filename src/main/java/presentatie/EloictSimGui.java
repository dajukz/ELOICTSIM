package presentatie;

import data.DataService;
import data.DeurService;
import data.InformatiepuntService;
import data.LokaalService;
import logica.Deur;
import logica.Informatiepunt;
import logica.Lokaal;
import logica.Meetkunde;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class EloictSimGui {

    //bron: https://huisstijl2.odisee.be/huisstijl/kleurenpalet-en-kleurbalans
    private static final Color SPELER_KLEUR =new Color(231,63,22);//Warmrood
    private static final Color LOKAAL_KLEUR =new Color(31,65,107);//Nachtblauw
    private static final Color DEUR_KLEUR =new Color(211,221,242);//Mistblauw

    private static int x = 20;
    private static int y = 260;
    private static int straal = 5;

    private JPanel tekenPanel;
    private JLabel titelLabel;
    private JPanel mainPanel;
    private static JFrame frame = new JFrame("ELO-ICT SIM - Youke Thomas");

    private Image background;
    private Image lokaalKlein, studentKlein, docentKlein;
    private ImageIcon lokaalGroot, studentGroot, docentGroot;


    public EloictSimGui() throws SQLException {
        DataService.init();

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int ingedrukteKnop = e.getKeyCode();
                int x2 = x, y2 = y;
                switch (ingedrukteKnop) {
                    case KeyEvent.VK_UP -> y2-=5;
                    case KeyEvent.VK_DOWN -> y2+=5;
                    case KeyEvent.VK_RIGHT -> x2+=5;
                    case KeyEvent.VK_LEFT -> x2-=5;
                }
                try {
                    List<Lokaal> lokalen = LokaalService.getLokalen();
                    for (Lokaal l: lokalen) {
                        boolean hitMuur = Meetkunde.cirkelOverlaptMetRechthoek(
                                l.getX(),
                                l.getY(),
                                l.getBreedte(),
                                -l.getLengte(),
                                x2,
                                y2,
                                straal
                        );
                        if (hitMuur) {
                            System.out.println("levend " + l);
                            List<Deur> deuren = DeurService.getDeuren();
                            boolean hitDeur = false;
                            for (Deur d : deuren) {
                                System.out.println("Deur: " + d);
                                hitDeur = Meetkunde.cirkelOverlaptMetRechthoek(
                                        d.getX1(),
                                        d.getX2(),
                                        d.getY1()-d.getX1(),
                                        d.getY2()-d.getX2(),
                                        x2,
                                        y2,
                                        straal+4
                                );
                                if (hitDeur) {
                                    break;
                                }
                            }
                            if (!hitDeur) {
                                return;
                            }
                        }
                    }
                    x = x2;
                    y = y2;
                } catch (SQLException ex) {
                    ex.printStackTrace(); //todo: popup foutmelding genereren
                }
                System.out.println(x + "  " + y);
                try {
                    List<Informatiepunt> informatiepunten = InformatiepuntService.getInformatiepunten();
                    for (Informatiepunt p: informatiepunten) {
                        boolean hit = Meetkunde.cirkelOverlaptMetRechthoek(
                                p.getX(),
                                p.getY(),
                                32,
                                32,
                                x,
                                y,
                                straal
                        );
                        if (hit) {
                            System.out.println("DOOD " + p);
                            titelLabel.setText(p.getBeschrijving());
                        }
                    }
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
                frame.repaint();
            }
        });
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
                drawCirkel(g2);
                drawLokalen(g2);
                drawDeuren(g2);
                drawInformatiePunten(g2);
            }

            //Het kan nuttig zijn om je tekenwerk op te splitsen en hier methoden toe te voegen om specifieke zaken te tekenen
            private void drawLokalen(Graphics2D g2) {
                g2.setColor(new Color(255,0,0));
                g2.drawRect(4, 226, 1000,  88);
            }

            private void drawDeuren(Graphics2D g2) {
                try {
                    g2.setColor(new Color(0,255,0));
                    List<Deur> deuren = DeurService.getDeuren();
                    for (Deur d : deuren) {
                        g2.drawRect(
                                d.getX1(),
                                d.getX2(),
                                d.getY1()-d.getX1(),
                                d.getY2()-d.getX2()
                                );
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            private void drawCirkel(Graphics2D g2) {
                g2.setColor(new Color(31,65,107));
                g2.fillOval(x-(straal+2), y-(straal+2), 2*(straal+2), 2*(straal+2)); //todo:startpunt aanpassen+overlap
            }

            private static void drawInformatiePunten(Graphics2D g2) {
                try {
                    List<Informatiepunt> informatiepunten = InformatiepuntService.getInformatiepunten();
                    for (Informatiepunt p: informatiepunten) {
                        g2.setColor(new Color(107,65,31));
                        g2.fillRect(p.getX(), p.getY(), 32, 32);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };
    }




    private static void moveCircle() {
        x = x+1;
        y = y+1;
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

    public static void main(String[] args) throws SQLException {
        frame.setContentPane(new EloictSimGui().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(laadAfbeelding("O-32x32"));
        frame.pack();
        frame.setVisible(true);
        if(frame.getTitle().contains("student")) JOptionPane.showMessageDialog(frame, "Zorg dat je naam in de titel van je JFrame staat. \n" + "Vervang hiervoor 'naam student' door je eigen naam in de code", "Pas je naam aan", JOptionPane.WARNING_MESSAGE);
    }
}
