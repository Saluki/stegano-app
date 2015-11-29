package ovh.gorillahack.steganoapp.algorithm;

import android.graphics.Bitmap;
import android.util.Base64;

import ovh.gorillahack.steganoapp.exceptions.SteganoEncodeException;

/**
 * Armored steganography encoder for adding a security layer using Base64
 */
public class ArmoredEncoder extends SteganoEncoder implements EncoderInterface<Bitmap> {

    public ArmoredEncoder(Bitmap immutableBitmap) {
        super(immutableBitmap);
    }

    @Override
    public Bitmap encode(String text) throws SteganoEncodeException {

        String armoredText = Base64.encodeToString(text.getBytes(), Base64.DEFAULT);
        return super.encode(armoredText);
    }

}
