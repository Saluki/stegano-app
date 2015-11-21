package ovh.gorillahack.steganoapp.algorithm;

import android.media.ExifInterface;

import java.io.IOException;
import java.security.InvalidParameterException;

public class SteganoTag extends ExifInterface {

    /**
     * Stegano length (in bits)
     */
    private static final String EXIF_LENGTH_TAG = "STEGANO_LENGTH";

    public SteganoTag(String filename) throws IOException {
        super(filename);
    }

    public int getLength() {
        return this.getAttributeInt(EXIF_LENGTH_TAG, 0);
    }

    public void setLength(int steganoLength) {

        if( steganoLength<0 ) {
            throw new InvalidParameterException("Stegano length cannot be negative");
        }

        String lengthValue = ""+steganoLength;
        this.setAttribute(EXIF_LENGTH_TAG, lengthValue);
    }

    public boolean hasLength() {
        return this.getAttribute(EXIF_LENGTH_TAG)!=null;
    }

}
