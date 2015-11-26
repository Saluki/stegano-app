package ovh.gorillahack.steganoapp.algorithm;

import android.graphics.Bitmap;

import ovh.gorillahack.steganoapp.exceptions.SteganoEncodeException;

public interface EncoderInterface {

    Bitmap encode(String text) throws SteganoEncodeException;
}
