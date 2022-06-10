package logica;

import logica.enums.Rol;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

/**
 * ELOICTSIM; Docent
 *
 * @author youke
 * @version 24/05/2022
 */
public class Docent extends Persoon{
    private String rol;

    public Docent(Integer id, String voornaam, String achternaam, String rol) {
        super(id, voornaam, achternaam);
        this.rol = rol;
    }

    @Override
    public String toString() {
        if (rol != null) {
            return "Functie= Docent"+ "\n" +
                    "Rol= " + rol;
        }
        return "Functie= Docent" + "\n";
    }
}
