package ovh.gorillahack.steganoapp.exceptions;

/**
 * Exception used by the steganography decoding process
 */
public class SteganoDecodeException extends Exception {

    public SteganoDecodeException(String message) {
        super(message);
    }
}
