package logica;

/**
 * ELOICTSIM; DevEnv
 *
 * @author youke
 * @version 29/05/2022
 */
public class DevEnv {
    private Integer seed;
    public DevEnv() {
        generateRandom(seed);
    }

    private String generateRandom(Integer seed) {
        return "asd";
    }
    /*
    De eloict sim is readonly, 2de gui is om dingen toe te voegen aan de db,
    daarvoor wachtwoord aanmaken om zo veranderingen tegen te houden in de dev env -> research over hoe dit moet
     */
}
