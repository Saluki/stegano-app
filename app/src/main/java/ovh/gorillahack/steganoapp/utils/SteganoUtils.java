package ovh.gorillahack.steganoapp.utils;

import android.graphics.Color;

public class SteganoUtils {

    static public int getLastSignificantBit(int standardColor) {

        int blueColor = Color.blue(standardColor);
        int bitMask = 1;

        return blueColor&bitMask;
    }

    static public int[] getBinarySequence(String text) {
        return null; // TODO
    }
}
