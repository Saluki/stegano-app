package ovh.gorillahack.steganoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

import ovh.gorillahack.steganoapp.algorithm.SteganoEncoder;

public class ShowGallery extends AppCompatActivity {
    Bitmap picChosen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_gallery);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                picChosen = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            //TODO: mettre le methode de la ligne suivante de utils (elle est impl dans ChoosePic)
            // buildTextViewPopUp(getString(R.string.error));
        }
        SteganoEncoder encoder = new SteganoEncoder(picChosen);
        String message = encoder.decode();
        //TODO: mettre le methode de la ligne suivante de utils (elle est impl dans ChoosePic
        String text = getString(R.string.this_is_the_mess) + message;
        // buildTextViewPopUp(getString(text));
    }

}

