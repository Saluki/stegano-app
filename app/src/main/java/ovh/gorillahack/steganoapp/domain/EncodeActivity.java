package ovh.gorillahack.steganoapp.domain;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ovh.gorillahack.steganoapp.R;
import ovh.gorillahack.steganoapp.algorithm.SteganoEncoder;
import ovh.gorillahack.steganoapp.exceptions.SteganoEncodeException;
import ovh.gorillahack.steganoapp.utils.Utils;

public class EncodeActivity extends AppCompatActivity {

    RelativeLayout layout;

    private static final int PICK_IMAGE = 1;
    private static final int TAKE_PICTURE = 2;
    private static final int LSB_STRENGTH_PROGRESS = 75;
    private static final int EXIF_STRENGTH_PROGRESS = 25;

    protected EditText messageMultipleInput;
    protected TextView countMessageLabel;
    protected ProgressBar algorithmStrengthBar;
    protected TextView algorithmStrengthText;
    protected RadioButton lsbRadioButton;
    protected RadioButton exifRadioButton;

    String messageToEncode = "";
    Bitmap pictureChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_pic);
        layout = (RelativeLayout) findViewById(R.id.choose_pic_layout);

        Utils.changeBackgroundColor(getSharedPreferences(Utils.PREFS_NAME, 0), layout);

        messageMultipleInput = (EditText) findViewById(R.id.messageMultipleInput);
        countMessageLabel = (TextView) findViewById(R.id.countMessageLabel);
        algorithmStrengthBar = (ProgressBar) findViewById(R.id.algorithmStrengthBar);
        algorithmStrengthText = (TextView) findViewById(R.id.algorithmStrengthText);
        lsbRadioButton = (RadioButton) findViewById(R.id.LsbRadioButton);
        exifRadioButton = (RadioButton) findViewById(R.id.ExifRadioButton);

        messageMultipleInput.addTextChangedListener(getInputLengthWatcher());

        lsbRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                algorithmStrengthBar.setProgress(LSB_STRENGTH_PROGRESS);
                algorithmStrengthText.setText(getString(R.string.algorithm_strength_strong));
            }
        });

        exifRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                algorithmStrengthBar.setProgress(EXIF_STRENGTH_PROGRESS);
                algorithmStrengthText.setText(getString(R.string.algorithm_strength_weak));
            }
        });
    }

    protected TextWatcher getInputLengthWatcher() {

        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int messageLength = messageMultipleInput.length();
                countMessageLabel.setText(messageLength+" / 1024");

                if( messageLength>1024 ) {
                    messageMultipleInput.setTextColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
    }

    public void launchCamera(View view) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath());
        startActivityForResult(intent, TAKE_PICTURE);
    }

    public void showGallery(View view) {

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType(Utils.INTENT_IMAGE_TYPE);

        Intent chooserIntent = Intent.createChooser(getIntent(), "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            Toast.makeText(getApplicationContext(), R.string.choosepic_result_failed, Toast.LENGTH_LONG).show();
            Log.e("Activity result error:", "result code: " + resultCode + " request code:" + requestCode);
            return;
        }

        if (requestCode == PICK_IMAGE) {

            if (data == null || data.getData() == null) {
                Utils.buildTextViewPopUp(this, getString(R.string.global_error_occurred), getString(R.string.error));
                Log.e("ChoosePicActivity", "Data format was null");
                return;
            }

            Uri uri = data.getData();
            try {
                this.pictureChosen = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_LONG).show();
                Log.e("ChoosePicActivity", "Could not retrieve media: " + e.getMessage());
                return;
            }

        } else if (requestCode == TAKE_PICTURE) {

            Bundle extras = data.getExtras();
            pictureChosen = (Bitmap) extras.get("data");

        } else {
            Utils.buildTextViewPopUp(this, getString(R.string.global_error_occurred), getString(R.string.error));
            Log.e("ChoosePicActivity", "Bad request code given: " + requestCode);
            return;
        }

        try {
            encodeMessageInBitmap();
        }
        catch(SteganoEncodeException e) {

            Toast errorToast = Toast.makeText(getBaseContext(), "Could not encode message:\n"+e.getMessage(), Toast.LENGTH_LONG);
            errorToast.show();

            Log.e("EncodeActivity", "Could not encode message: " + e.getMessage(), e);
            return;
        }

        Toast successToast = Toast.makeText(getBaseContext(), "Message successfully encoded", Toast.LENGTH_LONG);
        successToast.show();

        Intent mainIntent = new Intent(this, ovh.gorillahack.steganoapp.domain.MainActivity.class);
        startActivity(mainIntent);
    }

    protected void encodeMessageInBitmap() throws SteganoEncodeException {

        messageToEncode = messageMultipleInput.getText().toString();
        SteganoEncoder encoder = new SteganoEncoder(pictureChosen);
        String timeStamp = Utils.getCurrentTimeStamp();

        MediaStore.Images.Media.insertImage(getContentResolver(), pictureChosen, timeStamp + ".jpg", null);
        encoder.encode(messageToEncode);
    }
}
