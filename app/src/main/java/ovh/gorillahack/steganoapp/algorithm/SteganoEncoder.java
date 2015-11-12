package ovh.gorillahack.steganoapp.algorithm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

    public void encode(String text) throws SteganoEncodeException {


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
