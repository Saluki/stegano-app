package ovh.gorillahack.steganoapp.algorithm;

import ovh.gorillahack.steganoapp.exceptions.SteganoDecodeException;

public interface DecoderInterface {

    String decode() throws SteganoDecodeException;
}
