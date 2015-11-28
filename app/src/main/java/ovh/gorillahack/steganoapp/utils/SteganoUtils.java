package ovh.gorillahack.steganoapp.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import ovh.gorillahack.steganoapp.exceptions.SteganoDecodeException;
import ovh.gorillahack.steganoapp.exceptions.SteganoEncodeException;

public class SteganoUtils {

    public static final int UTF8_SIZE = 8;
    public static final int MARGIN_SIZE = 15;

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
        char[] stringMargin = Integer.toBinaryString(textLength*UTF8_SIZE).toCharArray();

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
            else {
                charPtr++;
            }
        }

        return textBuilder.toString();
    }

    public static int getIntegerBinaryList(ArrayList<Integer> binaryList) throws SteganoDecodeException {

        StringBuilder binaryString = new StringBuilder();
        for(Integer currentBit : binaryList) {
            binaryString.append(currentBit);
        }

        return Integer.parseInt(binaryString.toString(), 2);
    }

    public static void checkBitmap(Bitmap pictureBitmap) {

        if (pictureBitmap == null) {
            throw new NullPointerException("Picture bitmap cannot be null");
        }

        if (pictureBitmap.getWidth() == 0 || pictureBitmap.getHeight() == 0) {
            throw new InvalidParameterException("Picture must have a valid size");
        }
    }

    public static void debugCrasher(Bitmap b) {

        ArrayList<Integer> blueColors = new ArrayList<>();

        int CRASHER = 45;
        for (int y1 = 0; y1 < b.getHeight(); y1++) {
            for (int x1 = 0; x1 < b.getWidth(); x1++) {

                int blueColor = Color.blue(b.getPixel(x1, y1));
                blueColors.add(blueColor);

                if( CRASHER==0 ) {
                    break;
                }
                CRASHER--;
            }
            if( CRASHER==0 ) {
                break;
            }
        }

        // Point debugger here.
        blueColors.size();
    }

}