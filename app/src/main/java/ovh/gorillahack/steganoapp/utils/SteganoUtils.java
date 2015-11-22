package ovh.gorillahack.steganoapp.utils;

import android.graphics.Bitmap;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import ovh.gorillahack.steganoapp.exceptions.SteganoDecodeException;
import ovh.gorillahack.steganoapp.exceptions.SteganoEncodeException;

public class SteganoUtils {

    public static final int UTF8_SIZE = 8;
    public static final int MARGIN_SIZE = 91;

    public static int[] getBinarySequence(String text) {

        if( text==null ) {
            throw new NullPointerException("Given text cannot be null");
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

    public static int[] getMarginSequence(int textLength) throws SteganoEncodeException {

        if( textLength<0 ) {
            throw new SteganoEncodeException("Text length cannot be negative");
        }

        if( textLength>1024 ) {
            throw new SteganoEncodeException("Text length cannot exceed 1024 characters");
        }

        int[] binaryMargin = new int[MARGIN_SIZE];
        char[] stringMargin = Integer.toBinaryString(textLength).toCharArray();

        int binaryPtr = binaryMargin.length-1;
        for(int stringPtr=stringMargin.length-1; stringPtr>=0; stringPtr--) {

            binaryMargin[binaryPtr] = Integer.parseInt(""+stringMargin[stringPtr]);
            binaryPtr--;
        }

        return binaryMargin;
    }

    public static String getStringBinaryList(ArrayList<Integer> binaryList) throws SteganoDecodeException{

        int binaryListSize = binaryList.size();
        if( binaryListSize%UTF8_SIZE != 0 ) {
            throw new SteganoDecodeException("Wrong binary size, expected a divisor of "+UTF8_SIZE+" but got "+binaryListSize);
        }

        StringBuilder textBuilder = new StringBuilder();
        StringBuilder charBuilder = new StringBuilder();
        int charPtr = 0;

        for(Integer currentBit : binaryList) {

            String currentBitToString = ""+currentBit;
            charBuilder.append(currentBitToString);

            if( charPtr == UTF8_SIZE-1 ) {

                char decodedCharacter = (char) Integer.parseInt(charBuilder.toString(), 2);
                textBuilder.append(decodedCharacter);

                charBuilder = new StringBuilder();
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

}