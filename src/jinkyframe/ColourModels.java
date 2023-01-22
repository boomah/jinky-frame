package jinkyframe;

import java.awt.*;
import java.awt.image.IndexColorModel;
import java.util.Map;

import static jinkyframe.Colours.*;

public final class ColourModels {
    private ColourModels() {
        // static methods
    }

    public static final IndexColorModel fullColourModel = createFullColourModel();

    public static final IndexColorModel blackColourModel = createSingleColourModel(black);
    public static final IndexColorModel redColourModel = createSingleColourModel(red);

    public static final IndexColorModel blackYellowColourModel = createTwoColourModel(black, yellow);
    public static final IndexColorModel whiteGreenColourModel = createTwoColourModel(white, green);

    private static final Map<Color, IndexColorModel> colourMaps = Map.of(
            black, blackColourModel,
            red, redColourModel
    );

    private static IndexColorModel createSingleColourModel(Color colour) {
        byte max = (byte) 255;
        byte[] r = new byte[]{(byte) colour.getRed(), max};
        byte[] g = new byte[]{(byte) colour.getGreen(), max};
        byte[] b = new byte[]{(byte) colour.getBlue(), max};
        return new IndexColorModel(1, 2, r, g, b);
    }

    private static IndexColorModel createTwoColourModel(Color colour1, Color colour2) {
        byte[] r = new byte[]{(byte) colour1.getRed(), (byte) colour2.getRed()};
        byte[] g = new byte[]{(byte) colour1.getGreen(), (byte) colour2.getGreen()};
        byte[] b = new byte[]{(byte) colour1.getBlue(), (byte) colour2.getBlue()};
        return new IndexColorModel(1, 2, r, g, b);
    }

    private static IndexColorModel createFullColourModel() {
        byte max = (byte) 255;
        return new IndexColorModel(
                3,
                8,
                //          BLACK  WHITE GREEN  BLUE  RED  YELLOW     ORANGE     TAUPE
                new byte[]{0, max, 0, 0, max, max, max, (byte) 182},
                new byte[]{0, max, max, 0, 0, max, (byte) 200, (byte) 173},
                new byte[]{0, max, 0, max, 0, 0, 0, (byte) 158});
    }

    public static IndexColorModel get(Color colour) {
        return colourMaps.get(colour);
    }
}
