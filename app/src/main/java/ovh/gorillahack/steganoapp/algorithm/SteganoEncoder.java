package ovh.gorillahack.steganoapp.algorithm;

import android.graphics.Bitmap;

import java.security.InvalidParameterException;

import ovh.gorillahack.steganoapp.exceptions.SteganoEncodeException;
import ovh.gorillahack.steganoapp.utils.SteganoUtils;

public class SteganoEncoder {

    protected Bitmap pictureBitmap;

    public SteganoEncoder(Bitmap pictureBitmap) {

        if( pictureBitmap==null ) {
            throw new NullPointerException("Picture bitmap cannot be null");
        }

        if( pictureBitmap.getWidth()==0 || pictureBitmap.getHeight()==0 ) {
            throw new InvalidParameterException("Picture must have a valid size");
        }

        this.pictureBitmap = pictureBitmap;
    }

    public Bitmap encode(String text) throws SteganoEncodeException {

        if (text == null || text.isEmpty()) {
            throw new NullPointerException("Text to encode cannot be null or empty");
        }

        SteganoUtils.getBinarySequence(text);

        for (int y = 0; y < this.pictureBitmap.getHeight(); y++) {
            for (int x = 0; x < this.pictureBitmap.getWidth(); x++) {

                int argbColor = this.pictureBitmap.getPixel(x, y);
                int lsb = SteganoUtils.getLastSignificantBit(argbColor);

                // TODO Lsb manipulation


            }
        }

        return this.pictureBitmap;
    }

}
