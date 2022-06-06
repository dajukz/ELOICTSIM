package logica;

import java.awt.*;

/**
 * ELOICTSIM; Helper
 *
 * @author youke
 * @version 30/05/2022
 */
public class Helper {
    private static final int[] screenSize = getScreenBounds();

    private static int[] getScreenBounds() {
        int screenWidth = Toolkit
                .getDefaultToolkit()
                .getScreenSize()
                .width;
        int screenHeight = Toolkit
                .getDefaultToolkit()
                .getScreenSize()
                .height;
        return new int[]{screenWidth, screenHeight};
    }

    public static boolean isValidPoint(Punt teTestenPunt) {

        return true;
    }

    public static int[] getScreenSize() {
        return screenSize;
    }
}
