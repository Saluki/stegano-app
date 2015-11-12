package ovh.gorillahack.steganoapp.algorithm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import ovh.gorillahack.steganoapp.exceptions.BitmapTransformException;
import ovh.gorillahack.steganoapp.exceptions.SteganoEncodeException;

public class SteganoEncoder {

    protected Bitmap pictureBitmap;
    protected PictureSource pictureSource;

    public SteganoEncoder(final byte[] pictureData) throws BitmapTransformException {

        this.pictureSource = PictureSource.BYTE_ARRAY_SOURCE;
        this.pictureBitmap = this.transformFromByteArray(pictureData);
    }

    public SteganoEncoder(final String filePath) throws BitmapTransformException {

        this.pictureSource = PictureSource.FILE_SOURCE;
        this.pictureBitmap = this.loadPicture(filePath);
    }

    public SteganoEncoder(Bitmap pictureBitmap) {

        this.pictureSource = PictureSource.DIRECT_BITMAP;
        this.pictureBitmap = pictureBitmap;
    }

    public Bitmap encode(String text) throws SteganoEncodeException {

        if( text==null ) {
            throw new NullPointerException("Text to encode cannot be null");
        }

        this.getBinarySequence(text);

        for(int y=0; y<this.pictureBitmap.getHeight(); y++) {
            for(int x=0; x<this.pictureBitmap.getWidth(); x++) {

                int argbColor = this.pictureBitmap.getPixel(x, y);
                int lsb = this.getLastSignificantBit(argbColor);


            }
        }

        return this.pictureBitmap;
    }

    protected int[] getBinarySequence(String text) {
        return null; // TODO
    }

    protected int getLastSignificantBit(int standardColor) {

        int blueColor = Color.blue(standardColor);
        int bitMask = 1;

        return blueColor&bitMask;
    }

    protected Bitmap transformFromByteArray(final byte[] pictureData) throws BitmapTransformException{

        Bitmap pictureBitmap = BitmapFactory.decodeByteArray(pictureData, 0, pictureData.length);

        if(pictureBitmap==null) {
            throw new BitmapTransformException("Could not create bitmap from byte array");
        }
        return pictureBitmap;
    }

    protected Bitmap loadPicture(String pathName) throws BitmapTransformException {

        Bitmap pictureBitmap = BitmapFactory.decodeFile(pathName);

        if( pictureBitmap==null ) {
            throw new BitmapTransformException("Could not create bitmap from path "+pathName);
        }
        return pictureBitmap;
    }

    public enum PictureSource {
        BYTE_ARRAY_SOURCE, FILE_SOURCE, DIRECT_BITMAP
    }
}
