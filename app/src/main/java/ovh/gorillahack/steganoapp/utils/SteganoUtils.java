package ovh.gorillahack.steganoapp.utils;

import android.graphics.Bitmap;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import ovh.gorillahack.steganoapp.exceptions.SteganoDecodeException;
import ovh.gorillahack.steganoapp.exceptions.SteganoEncodeException;

/**
 * Various utilities that simplify the steganography processes
 */
public class SteganoUtils {

    /**
     * Size of a UTF-8 character in bits.
     */
    public static final int UTF8_SIZE = 8;

    /**
     * The size of the margin in a bitmap.
     * The margin bits are the first N pixels that are used to store the binary length of the text.
     */
    public static final int MARGIN_SIZE = 15;

    /**
     * Converts a string to his binary equivalent.
     *
     * @param text The string that must be converted, cannot be null
     * @return An array of integers representing the binary equivalent
     */
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

    /**
     * Returns the margin array which represents the text length (in bits).
     *
     * @param textLength The length of a given text (the number of characters)
     * @return An array representing the margin as a suite of bits
     * @throws SteganoEncodeException If the given text length is negative or exceeds 1024
     */
    public static int[] getMarginSequence(int textLength) throws SteganoEncodeException {

        if( textLength<0 ) {
            throw new SteganoEncodeException("Text length cannot be negative");
        }

        if( textLength>1024 ) {
            throw new SteganoEncodeException("Text length cannot exceed 1024 characters");
        }

        int[] binaryMargin = new int[MARGIN_SIZE];
        char[] stringMargin = Integer.toBinaryString(textLength*UTF8_SIZE).toCharArray();

        int binaryPtr = binaryMargin.length-1;
        for(int stringPtr=stringMargin.length-1; stringPtr>=0; stringPtr--) {

            binaryMargin[binaryPtr] = Integer.parseInt(""+stringMargin[stringPtr]);
            binaryPtr--;
        }

        return binaryMargin;
    }

    /**
     * Reconverts an array of bits (represented as integers) to a readable UTF-8 string
     *
     * @param binaryList An array containing a suite of integers, which represents the bits of a string
     * @return The readable UTF-8 string
     * @throws SteganoDecodeException If the array of bits isn't a multiple of an UTF-8 character
     */
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
            else {
                charPtr++;
            }
        }

        return textBuilder.toString();
    }

    /**
     * Converts an array of bits (represented with integers) to an integer.
     *
     * This method is mainly used to decode the margin of an encoded image.
     *
     * @param binaryList An array of integers which represents a number in binary format
     * @return The converted integer
     */
    public static int getIntegerBinaryList(ArrayList<Integer> binaryList) {

        StringBuilder binaryString = new StringBuilder();
        for(Integer currentBit : binaryList) {
            binaryString.append(currentBit);
        }

        return Integer.parseInt(binaryString.toString(), 2);
    }

    /**
     * Performs some basic check-ups on a given bitmap picture.
     *
     * @param pictureBitmap The bitmap that must be checked
     */
    public static void checkBitmap(Bitmap pictureBitmap) {

        if (pictureBitmap == null) {
            throw new NullPointerException("Picture bitmap cannot be null");
        }

        if (pictureBitmap.getWidth() == 0 || pictureBitmap.getHeight() == 0) {
            throw new InvalidParameterException("Picture must have a valid size");
        }
    }
}