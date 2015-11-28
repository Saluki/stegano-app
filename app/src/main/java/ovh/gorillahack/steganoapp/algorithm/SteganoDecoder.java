package ovh.gorillahack.steganoapp.algorithm;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;

import ovh.gorillahack.steganoapp.exceptions.SteganoDecodeException;
import ovh.gorillahack.steganoapp.utils.SteganoUtils;

public class SteganoDecoder implements DecoderInterface {

    private Bitmap pictureBitmap;

    public SteganoDecoder(Bitmap pictureBitmap) {

        SteganoUtils.checkBitmap(pictureBitmap);
        this.pictureBitmap = pictureBitmap;
    }

    public String decode() throws SteganoDecodeException {

        ArrayList<Integer> extractedBits = new ArrayList<>();
        ArrayList<Integer> extractedMargin = new ArrayList<>();
        int decodePtr = 0;
        int dataSize = 0;

        for (int y = 0; y < this.pictureBitmap.getHeight(); y++) {
            for (int x = 0; x < this.pictureBitmap.getWidth(); x++) {

                if( decodePtr<SteganoUtils.MARGIN_SIZE ) {

                    int blueColor = Color.blue(this.pictureBitmap.getPixel(x, y));
                    extractedMargin.add(blueColor%2);

                    if( decodePtr==SteganoUtils.MARGIN_SIZE-1 ) {
                        dataSize = SteganoUtils.getIntegerBinaryList(extractedMargin);
                    }
                }
                else {  // Decoding data

                    if( dataSize==0 ) {
                        return SteganoUtils.getStringBinaryList(extractedBits);
                    }

                    int argbColor = this.pictureBitmap.getPixel(x, y);
                    int blueColor = Color.blue(argbColor);

                    extractedBits.add(blueColor%2);

                    dataSize--;
                }

                decodePtr++;
            }
        }

        return SteganoUtils.getStringBinaryList(extractedBits);
    }
}