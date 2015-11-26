package ovh.gorillahack.steganoapp.algorithm;

import android.graphics.Bitmap;
import android.graphics.Color;

import ovh.gorillahack.steganoapp.exceptions.SteganoEncodeException;
import ovh.gorillahack.steganoapp.utils.SteganoUtils;
import ovh.gorillahack.steganoapp.utils.Utils;

public class SteganoEncoder implements EncoderInterface {

    protected Bitmap pictureBitmap;

    public SteganoEncoder(Bitmap immutableBitmap) {

        SteganoUtils.checkBitmap(immutableBitmap);
        this.pictureBitmap = immutableBitmap.copy(immutableBitmap.getConfig(), true);
    }

    public Bitmap encode(String text) throws SteganoEncodeException {

        if (text == null || text.isEmpty()) {
            throw new SteganoEncodeException("Message cannot be empty");
        }

        int[] binarySequence = SteganoUtils.getBinarySequence(text);
        int[] marginSequence = SteganoUtils.getMarginSequence(text.length());
        int binaryPtr = 0;
        int marginPtr = 0;

        // For each pixel in the picture
        for (int y = 0; y < this.pictureBitmap.getHeight(); y++) {
            for (int x = 0; x < this.pictureBitmap.getWidth(); x++) {

                if( marginPtr < SteganoUtils.MARGIN_SIZE ) {

                    this.encodeMargin(marginSequence, marginPtr, y, x);
                    marginPtr++;
                }
                else if (binaryPtr >= binarySequence.length-1) {

                    // Return the new bitmap when all data is encoded
                    return this.pictureBitmap;
                }
                else {

                    // Encode the data and move the bit pointer
                    encodeData(binarySequence, binaryPtr, y, x);
                    binaryPtr++;
                }
            }
        }

        return this.pictureBitmap;
    }

    private void encodeMargin(int[] marginSequence, int marginPtr, int y, int x) {

        int argbColor = this.pictureBitmap.getPixel(x, y);
        int blueColor = Color.blue(argbColor);

        if (blueColor % 2 == 1) {
            blueColor--;
        }
        blueColor += marginSequence[marginPtr];

        int newArgbColor = Color.argb(Color.alpha(argbColor),
                Color.red(argbColor),
                Color.green(argbColor),
                blueColor);
        this.pictureBitmap.setPixel(x, y, newArgbColor);
    }

    private void encodeData(int[] binarySequence, int binaryPtr, int y, int x) {

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
    }

}
