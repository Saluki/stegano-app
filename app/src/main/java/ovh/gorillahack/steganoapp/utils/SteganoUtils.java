package ovh.gorillahack.steganoapp.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.security.InvalidParameterException;
import java.util.ArrayList;

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

    static public String getStringBinaryList(ArrayList<Integer> binaryList) {

        return binaryList.toString();   // TODO
    }

    static public void checkBitmap(Bitmap pictureBitmap) {

        if (pictureBitmap == null) {
            throw new NullPointerException("Picture bitmap cannot be null");
        }

        if (pictureBitmap.getWidth() == 0 || pictureBitmap.getHeight() == 0) {
            throw new InvalidParameterException("Picture must have a valid size");
        }

        // Everything OK
    }
}
