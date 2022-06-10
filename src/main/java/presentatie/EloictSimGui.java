package presentatie;

import data.DataService;
import data.DeurService;
import data.InformatiepuntService;
import data.LokaalService;
import logica.*;

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

    private static int x = 40;
    private static int y = 290;
    private static int straal = 5;

    private JPanel tekenPanel;
    private JLabel titelLabel;
    private JPanel mainPanel;
    private JPanel panelInfo;
    private JTextArea korteTextArea;
    private JLabel lijstLabel;
    private JLabel langeTekstLabel;
    private JTextArea lijstTextArea;
    private JTextArea langeTextArea;
    private JLabel startLabel;
    private JTextArea startTextArea;
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
                                l.getLengte(),
                                x2,
                                y2,
                                straal
                        );
                        if (hitMuur) {
                            List<Deur> deuren = DeurService.getDeuren();
                            boolean hitDeur = false;
                            for (Deur d : deuren) {
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
                    ex.printStackTrace();
                }
                try {
                    List<Informatiepunt> informatiepunten = InformatiepuntService.getInformatiepunten();
                    boolean hit;
                    for (Informatiepunt p: informatiepunten) {
                        hit = Meetkunde.cirkelOverlaptMetRechthoek(
                                p.getX(),
                                p.getY(),
                                32,
                                32,
                                x,
                                y,
                                straal
                        );
                        if (hit) {
                            setmededeling(p);
                        }
                    }
                    hit = false;
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
                frame.repaint();
            }

            private void setmededeling(Informatiepunt p) {
                if (p.isPersoon()) {
                    StringBuilder stringBuilder = new StringBuilder("");
                    if (p.getPersoon() instanceof Student s) {
                        titelLabel.setIcon(studentGroot);
                        titelLabel.setText(
                                stringBuilder
                                .append(s.getVoornaam().substring(0, 1).toUpperCase())
                                .append(s.getVoornaam().substring(1))
                                .append(" ")
                                .append(s.getAchternaam().substring(0, 1).toUpperCase())
                                .append(s.getAchternaam().substring(1))
                                .toString()
                        );
                        korteTextArea.setText(s.toString());
                        lijstLabel.setText("Gevolgde vakken");
                        StringBuilder lijstText = new StringBuilder("");
                        for (Vak vak: s.getVakken()) {
                            lijstText.append(vak.toString()).append("\n");
                        }
                        lijstTextArea.setText(lijstText.toString());
                        langeTekstLabel.setText("Extra over " + stringBuilder);
                        langeTextArea.setText(p.getBeschrijving());
                    } else if (p.getPersoon() instanceof Docent d) {
                        titelLabel.setIcon(docentGroot);
                        titelLabel.setText(
                                stringBuilder
                                .append(d.getVoornaam().substring(0, 1).toUpperCase())
                                .append(d.getVoornaam().substring(1))
                                .append(" ")
                                .append(d.getAchternaam().substring(0, 1).toUpperCase())
                                .append(d.getAchternaam().substring(1))
                                .toString()
                        );
                        korteTextArea.setText(d.toString());
                        lijstLabel.setText("Lessen gegeven");
                        StringBuilder lijstText = new StringBuilder("");
                        for (Vak vak: d.getVakken()) {
                            lijstText.append(vak.toString()).append("\n");
                        }
                        lijstTextArea.setText(lijstText.toString());
                        langeTekstLabel.setText("Extra over " + stringBuilder);
                        langeTextArea.setText(p.getBeschrijving());
                    }
                } else if (p.isLokaal()) {
                    if (p.getLokaal().getId().equals(14)) {
                        startLabel.setText("Info start spel");
                        startTextArea.setText(p.getBeschrijving());
                    }
                    StringBuilder stringBuilder = new StringBuilder("");
                    titelLabel.setIcon(lokaalGroot);
                    titelLabel.setText(
                            stringBuilder
                            .append(p.getLokaal().getNaam().substring(0, 1).toUpperCase())
                            .append(p.getLokaal().getNaam().substring(1))
                            .toString()
                    );
                    korteTextArea.setText(p.getLokaal().getLokaalcode());
                    lijstLabel.setText("Lessen gegeven");
                    StringBuilder lijstText = new StringBuilder("");
                    for (Vak vak: p.getLokaal().getVakken()) {
                        lijstText.append(vak.toString()).append("\n");
                    }
                    lijstTextArea.setText(lijstText.toString());
                    langeTekstLabel.setText("Extra over " + stringBuilder);
                    langeTextArea.setText(p.getBeschrijving());
                }
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
                drawLokalen(g2);
                drawDeuren(g2);
                drawInformatiePunten(g2);
                drawCirkel(g2);
            }

            //Het kan nuttig zijn om je tekenwerk op te splitsen en hier methoden toe te voegen om specifieke zaken te tekenen
            private void drawLokalen(Graphics2D g2) {
                g2.setColor(LOKAAL_KLEUR);
                g2.setStroke(new BasicStroke(8));
                g2.drawRect(4, 226, 1000,  88);
                try {
                    List<Lokaal> lokalen = LokaalService.getLokalen();
                    for (Lokaal l: lokalen) {
                        g2.drawRect(l.getX(), l.getY(), l.getBreedte(), l.getLengte());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            private void drawDeuren(Graphics2D g2) {
                try {
                    g2.setColor(DEUR_KLEUR);
                    g2.setStroke(new BasicStroke(8));
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
                g2.setColor(SPELER_KLEUR);
                g2.fillOval(x-(straal+2), y-(straal+2), 2*(straal+2), 2*(straal+2)); //todo:startpunt aanpassen+overlap
            }

            private void drawInformatiePunten(Graphics2D g2) {
                try {
                    List<Informatiepunt> informatiepunten = InformatiepuntService.getInformatiepunten();
                    for (Informatiepunt p: informatiepunten) {
                        if (p.isPersoon()) {
                            if (p.getPersoon() instanceof Student s) {
                                g2.drawImage(studentKlein, p.getX(), p.getY(), null);
                            } else if (p.getPersoon() instanceof Docent d){
                                g2.drawImage(docentKlein, p.getX(), p.getY(), null);
                            }
                        } else if (p.isLokaal()) {
                            g2.drawImage(lokaalKlein ,p.getX(), p.getY(), null);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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

    public static void main(String[] args) throws SQLException {
        frame.setContentPane(new EloictSimGui().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(laadAfbeelding("O-32x32"));
        frame.pack();
        frame.setVisible(true);
        if(frame.getTitle().contains("student")) JOptionPane.showMessageDialog(frame, "Zorg dat je naam in de titel van je JFrame staat. \n" + "Vervang hiervoor 'naam student' door je eigen naam in de code", "Pas je naam aan", JOptionPane.WARNING_MESSAGE);
    }
}
