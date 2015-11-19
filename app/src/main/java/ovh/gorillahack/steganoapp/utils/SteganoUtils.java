package ovh.gorillahack.steganoapp.utils;

import android.graphics.Bitmap;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import ovh.gorillahack.steganoapp.exceptions.SteganoDecodeException;
import ovh.gorillahack.steganoapp.exceptions.SteganoEncodeException;

public class SteganoUtils {

    public static final int UTF8_SIZE = 8;

    public static int[] getBinarySequence(String text) {

        if( text==null ) {
            throw new NullPointerException("Text cannot be null");
        }

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

    public static String getStringBinaryList(ArrayList<Integer> binaryList) throws SteganoDecodeException{

        if( binaryList.size()%UTF8_SIZE != 0 ) {
            throw new SteganoDecodeException("Wrong binary size");
        }

        StringBuilder textBuilder = new StringBuilder();
        StringBuilder charBuilder = new StringBuilder();
        int charPtr = 0;

        for(Integer currentBit : binaryList) {

            charBuilder.append(""+currentBit);

            if( charPtr == UTF8_SIZE-1 ) {
                textBuilder.append(Integer.parseInt(charBuilder.toString()));
                charPtr = 0;
            }
            charPtr++;
        }

        return textBuilder.toString();
    }

    public static void checkBitmap(Bitmap pictureBitmap) {

        if (pictureBitmap == null) {
            throw new NullPointerException("Picture bitmap cannot be null");
        }

        if (pictureBitmap.getWidth() == 0 || pictureBitmap.getHeight() == 0) {
            throw new InvalidParameterException("Picture must have a valid size");
        }
    }

    public static void checkTextFitting(Bitmap pictureBitmap, String text) throws SteganoEncodeException {

        if( text==null || pictureBitmap==null ) {
            throw new NullPointerException("Bitmap or text could not be null");
        }

        int textSize = text.length()*UTF8_SIZE;
        int pictureSize = pictureBitmap.getWidth()*pictureBitmap.getHeight();

        if( textSize >= pictureSize ) {
            throw new SteganoEncodeException("Text size too large for picture");
        }
    }
}
