package ovh.gorillahack.steganoapp.algorithm;

import android.graphics.Bitmap;
import android.util.Base64;

import ovh.gorillahack.steganoapp.exceptions.SteganoDecodeException;

/**
 * Armored steganography decoder for adding a security layer using Base64
 */
public class ArmoredDecoder extends SteganoDecoder implements DecoderInterface {

    public ArmoredDecoder(Bitmap immutableBitmap) {
        super(immutableBitmap);
    }

    @Override
    public String decode() throws SteganoDecodeException {

        String armoredText = super.decode();
        return new String(Base64.decode(armoredText, Base64.DEFAULT));
    }
}
