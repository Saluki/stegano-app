package ovh.gorillahack.steganoapp.algorithm;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;

import ovh.gorillahack.steganoapp.exceptions.SteganoDecodeException;
import ovh.gorillahack.steganoapp.utils.SteganoUtils;

public class SteganoDecoder {

    @Deprecated
    private static final int DECODE_LIMIT_DEV = SteganoUtils.UTF8_SIZE*10;

    private Bitmap pictureBitmap;

    public SteganoDecoder(Bitmap pictureBitmap) {

        SteganoUtils.checkBitmap(pictureBitmap);
        this.pictureBitmap = pictureBitmap;
    }

    public String decode() throws SteganoDecodeException {

        ArrayList<Integer> extractedBits = new ArrayList<>();
        int decodePtr = 0;

        for (int y = 0; y < this.pictureBitmap.getHeight(); y++) {
            for (int x = 0; x < this.pictureBitmap.getWidth(); x++) {

                if( decodePtr>=DECODE_LIMIT_DEV ) {
                    return SteganoUtils.getStringBinaryList(extractedBits);
                }
                decodePtr++;

                int argbColor = this.pictureBitmap.getPixel(x, y);
                int blueColor = Color.blue(argbColor);

                extractedBits.add(blueColor%2);
            }
        }

        return SteganoUtils.getStringBinaryList(extractedBits);
    }
}
