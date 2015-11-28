package ovh.gorillahack.steganoapp.domain;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.IOException;

import ovh.gorillahack.steganoapp.R;
import ovh.gorillahack.steganoapp.algorithm.SteganoDecoder;
import ovh.gorillahack.steganoapp.utils.Utils;

public class DecodeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

    protected RelativeLayout layout;
    protected Bitmap picChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_decode);
        layout = (RelativeLayout) findViewById(R.id.show_gallery_layout);

        Utils.changeBackgroundColor(getSharedPreferences(Utils.PREFS_NAME, 0), layout);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                picChosen = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                Utils.buildTextViewPopUp(this, getString(R.string.global_error_occurred), getString(R.string.no_image_retreived));
                e.printStackTrace();
            }
        } else {
            Utils.buildTextViewPopUp(this, getString(R.string.global_error_occurred), getString(R.string.no_image_retreived));
            return;
        }
        try {
            String message = (new SteganoDecoder(this.picChosen)).decode();
            String text = getString(R.string.this_is_the_mess) + message;
            Utils.buildTextViewPopUp(this, getString(R.string.info), text);
        }
        catch (Exception e) {
            Log.e("DecodeActivity", "Could not decode text in image: "+e.getMessage(), e);
            Utils.buildTextViewPopUp(this, getString(R.string.global_error_occurred), getString(R.string.decode_decoding_error));
        }
    }

    public void showGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

}

