package ovh.gorillahack.steganoapp.algorithm;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;

import ovh.gorillahack.steganoapp.utils.SteganoUtils;

public class SteganoDecoder {

    private static final int DECODE_LIMIT_DEV = SteganoUtils.UTF8_SIZE*10;

    private Bitmap pictureBitmap;

    public SteganoDecoder(Bitmap pictureBitmap) {

        SteganoUtils.checkBitmap(pictureBitmap);
        this.pictureBitmap = pictureBitmap;
    }

    public String decode() {

        ArrayList<Integer> extractedBits = new ArrayList<>();

        for (int y = 0; y < this.pictureBitmap.getHeight(); y++) {
            for (int x = 0; x < this.pictureBitmap.getWidth(); x++) {

                int argbColor = this.pictureBitmap.getPixel(x, y);
                int blueColor = Color.blue(argbColor);

                extractedBits.add(blueColor%2);
            }
        }

        return SteganoUtils.getStringBinaryList(extractedBits);
    }
}
