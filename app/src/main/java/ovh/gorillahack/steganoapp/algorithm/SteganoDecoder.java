package ovh.gorillahack.steganoapp.algorithm;

import android.graphics.Bitmap;

import ovh.gorillahack.steganoapp.utils.SteganoUtils;

public class SteganoDecoder {

    private Bitmap pictureBitmap;

    public SteganoDecoder(Bitmap pictureBitmap) {
        this.pictureBitmap = pictureBitmap;
    }

    public String decode() {

        StringBuilder extractedText = new StringBuilder();

        for (int y = 0; y < this.pictureBitmap.getHeight(); y++) {
            for (int x = 0; x < this.pictureBitmap.getWidth(); x++) {

                int argbColor = this.pictureBitmap.getPixel(x, y);
                int lsb = SteganoUtils.getLastSignificantBit(argbColor);

                // TODO Lsb extraction
            }
        }

        return extractedText.toString();
    }
}
