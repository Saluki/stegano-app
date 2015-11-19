package ovh.gorillahack.steganoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.IOException;

import ovh.gorillahack.steganoapp.algorithm.SteganoDecoder;
import ovh.gorillahack.steganoapp.utils.Utils;

public class DecodeActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;

    private static final String INTENT_IMAGE_TYPE = "image/*";
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
            Utils.buildTextViewPopUp(this, getString(R.string.error));
            return;
        }
        String message = null;
        try {
            message = (new SteganoDecoder(this.picChosen)).decode();
        } catch (Exception e) {

        }
        String text = getString(R.string.this_is_the_mess) + message;

        Utils.buildTextViewPopUp(this, text);
    }

    public void showGallery(View view) {

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType(INTENT_IMAGE_TYPE);

        Intent chooserIntent = Intent.createChooser(getIntent(), "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }
}

