package ovh.gorillahack.steganoapp.algorithm;

import android.graphics.Bitmap;

import ovh.gorillahack.steganoapp.exceptions.SteganoEncodeException;

public class ExifEncoder implements EncoderInterface<Bitmap> {

    @Override
    public Bitmap encode(String text) throws SteganoEncodeException {
        throw new SteganoEncodeException("Algorithm not yet implemented");
    }

}
