package ovh.gorillahack.steganoapp.domain;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;

import ovh.gorillahack.steganoapp.R;
import ovh.gorillahack.steganoapp.algorithm.ArmoredDecoder;
import ovh.gorillahack.steganoapp.algorithm.DecoderInterface;
import ovh.gorillahack.steganoapp.algorithm.SteganoDecoder;
import ovh.gorillahack.steganoapp.utils.Utils;

public class DecodeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

    protected RelativeLayout layout;
    protected Bitmap pictureChosen;

    protected RadioButton lsbRadioButton;
    protected RadioButton armoredRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_decode);
        layout = (RelativeLayout) findViewById(R.id.show_gallery_layout);

        Utils.changeBackgroundColor(getSharedPreferences(Utils.PREFS_NAME, 0), layout);

        lsbRadioButton = (RadioButton) findViewById(R.id.lsbRadioButton);
        armoredRadioButton = (RadioButton) findViewById(R.id.armoredRadioButton);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                pictureChosen = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), R.string.choosepic_result_failed, Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.choosepic_result_failed, Toast.LENGTH_LONG).show();
            return;
        }

        DecoderInterface decoder;

        if (lsbRadioButton.isChecked()) {
            decoder = new SteganoDecoder(pictureChosen);
        } else if (armoredRadioButton.isChecked()) {
            decoder = new ArmoredDecoder(pictureChosen);
        } else {
            Utils.buildTextViewPopUp(this, getString(R.string.global_error_occurred), getString(R.string.no_algorithm_selected));
            return;
        }

        try {
            String message = decoder.decode();
            Utils.buildTextViewPopUp(this, getString(R.string.stegano_message_decoded), message);
        }
        catch (Exception e) {
            Utils.buildTextViewPopUp(this, getString(R.string.global_error_occurred), getString(R.string.decode_decoding_error));
        }
    }

    public void showGallery(View view) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

}

