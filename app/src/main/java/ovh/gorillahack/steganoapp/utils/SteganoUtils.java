package ovh.gorillahack.steganoapp.utils;

import android.graphics.Color;

public class SteganoUtils {

    public static final int UTF8_SIZE = 8;

    static public int[] getBinarySequence(String text) {

        char[] textArray = text.toCharArray();
        int[] bitList = new int[textArray.length * UTF8_SIZE];
        int bitListPtr = 0;

        for (char asciiValue : textArray) {

            for (int bitWise = UTF8_SIZE - 1; bitWise >= 0; bitWise--) {
                int tempBit = ((asciiValue & (1 << bitWise)) > 0) ? 1 : 0;

                bitList[bitListPtr] = tempBit;
                bitListPtr++;
            }
        }

        return bitList;
    }
}
