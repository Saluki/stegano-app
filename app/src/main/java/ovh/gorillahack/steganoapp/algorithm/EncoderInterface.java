package ovh.gorillahack.steganoapp.algorithm;

import ovh.gorillahack.steganoapp.exceptions.SteganoEncodeException;

public interface EncoderInterface<T> {

    T encode(String text) throws SteganoEncodeException;
}
