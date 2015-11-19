package ovh.gorillahack.steganoapp.algorithm;

import android.graphics.Bitmap;
import android.graphics.Color;

import ovh.gorillahack.steganoapp.exceptions.SteganoEncodeException;
import ovh.gorillahack.steganoapp.utils.SteganoUtils;

public class SteganoEncoder {

    protected Bitmap pictureBitmap;

    public SteganoEncoder(Bitmap immutableBitmap) {

        SteganoUtils.checkBitmap(immutableBitmap);
        this.pictureBitmap = immutableBitmap.copy(immutableBitmap.getConfig(), true);
    }

    public Bitmap encode(String text) throws SteganoEncodeException {

        if (text == null || text.isEmpty()) {
            throw new NullPointerException("Text to encode cannot be null or empty");
        }

        SteganoUtils.checkTextFitting(this.pictureBitmap, text);

        int[] binarySequence = SteganoUtils.getBinarySequence(text);
        int binaryPtr = 0;

        // For each pixel in the picture
        for (int y = 0; y < this.pictureBitmap.getHeight(); y++) {
            for (int x = 0; x < this.pictureBitmap.getWidth(); x++) {

                // Extract Least Significant Byte from pixel (blue component)
                int argbColor = this.pictureBitmap.getPixel(x, y);
                int blueColor = Color.blue(argbColor);

                // Make the last bit 0 if needed
                if (blueColor % 2 == 1) {
                    blueColor--;
                }

                // There is the true encoding: injecting the data
                blueColor += binarySequence[binaryPtr];

                // Recode the color in ARGB format
                int newArgbColor = Color.argb(Color.alpha(argbColor),
                        Color.red(argbColor),
                        Color.green(argbColor),
                        blueColor);
                this.pictureBitmap.setPixel(x, y, newArgbColor);

                // Return the new bitmap when all data is encoded
                if (binaryPtr >= binarySequence.length - 1) {
                    return this.pictureBitmap;
                }
                // Move the bit pointer
                binaryPtr++;
            }
        }

        return this.pictureBitmap;
    }

}
