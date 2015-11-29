package ovh.gorillahack.steganoapp.domain;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ovh.gorillahack.steganoapp.R;
import ovh.gorillahack.steganoapp.algorithm.EncoderInterface;
import ovh.gorillahack.steganoapp.algorithm.ArmoredEncoder;
import ovh.gorillahack.steganoapp.algorithm.SteganoEncoder;
import ovh.gorillahack.steganoapp.exceptions.SteganoEncodeException;
import ovh.gorillahack.steganoapp.utils.Utils;

public class EncodeActivity extends AppCompatActivity {

    RelativeLayout layout;

    private static final int PICK_IMAGE = 1;
    private static final int TAKE_PICTURE = 2;
    private static final int LSB_STRENGTH_PROGRESS = 40;
    private static final int ARMORED_STRENGTH_PROGRESS = 75;

    protected EditText messageToEncodeET;
    protected TextView countMessageLabel;
    protected ProgressBar algorithmStrengthBar;
    protected TextView algorithmStrengthText;
    protected RadioButton lsbRadioButton;
    protected RadioButton armoredRadioButton;

    String messageToEncode = "";
    Bitmap pictureChosen;
    int easterEgg = 0;
    int checkBoxSwitch = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_encode);
        layout = (RelativeLayout) findViewById(R.id.choose_pic_layout);

        Utils.changeBackgroundColor(getSharedPreferences(Utils.PREFS_NAME, 0), layout);

        messageToEncodeET = (EditText) findViewById(R.id.messageMultipleInput);
        countMessageLabel = (TextView) findViewById(R.id.countMessageLabel);
        algorithmStrengthBar = (ProgressBar) findViewById(R.id.algorithmStrengthBar);
        algorithmStrengthText = (TextView) findViewById(R.id.algorithmStrengthText);
        lsbRadioButton = (RadioButton) findViewById(R.id.LsbRadioButton);
        armoredRadioButton = (RadioButton) findViewById(R.id.ArmoredRadioButton);

        messageToEncodeET.addTextChangedListener(getInputLengthWatcher());

        lsbRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkBoxSwitch == 1) {
                    easterEgg++;
                    checkBoxSwitch = 0;
                    if (easterEgg == 4) //if user switched algo 4 times
                        layout.setBackgroundResource(R.drawable.grumpycat);
                    if (easterEgg > 3)
                        layout.getBackground().setAlpha(easterEgg * 10); //everytime he switches add alpha to new backround
                }

                algorithmStrengthBar.setProgress(LSB_STRENGTH_PROGRESS);
                algorithmStrengthText.setText(getString(R.string.algorithm_strength_weak));
            }
        });

        armoredRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkBoxSwitch == 0) {
                    checkBoxSwitch = 1;
                }

                algorithmStrengthBar.setProgress(ARMORED_STRENGTH_PROGRESS);
                algorithmStrengthText.setText(getString(R.string.algorithm_strength_strong));
            }
        });
    }

    protected TextWatcher getInputLengthWatcher() {

        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int messageLength = messageToEncodeET.length();
                countMessageLabel.setText(messageLength + " / 1024");

                if (messageLength > 1024) {
                    messageToEncodeET.setTextColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
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

        // Handle bad retrieval of picture
        if (resultCode != RESULT_OK) {
            Toast.makeText(getApplicationContext(), R.string.choosepic_result_failed, Toast.LENGTH_LONG).show();
            Log.e("Activity result error:", "result code: " + resultCode + " request code:" + requestCode);
            return;
        }

        // Extract picture bitmap based on source
        if (requestCode == PICK_IMAGE) {

            if (data == null || data.getData() == null) {
                Utils.buildTextViewPopUp(this, getString(R.string.global_error_occurred), getString(R.string.error));
                Log.e("EncodeActivity", "Data format was null");
                return;
            }
            Uri uri = data.getData();
            try {
                pictureChosen = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_LONG).show();
                Log.e("EncodeActivity", "Could not retrieve media: " + e.getMessage());
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

        // Encode the message
        try {
            encodeMessageInBitmap();
        }
        catch (SteganoEncodeException e) {
            Toast errorToast = Toast.makeText(getBaseContext(), "Could not encode message:\n" + e.getMessage(), Toast.LENGTH_LONG);
            errorToast.show();
            Log.e("EncodeActivity", "Could not encode message: " + e.getMessage(), e);
            return;
        }

        // Reset form elements
        this.messageToEncodeET.setText("");

        // Displays a feedback for the user
        Utils.buildTextViewPopUp(this, getString(R.string.encoding_success_title), getString(R.string.encoding_success_body));
    }

    protected void encodeMessageInBitmap() throws SteganoEncodeException {

        messageToEncode = messageToEncodeET.getText().toString(); //get message from Edit Text
        EncoderInterface encoder;

        if (lsbRadioButton.isChecked()) {
            encoder = new SteganoEncoder(pictureChosen);
        } else if (armoredRadioButton.isChecked()) {
            encoder = new ArmoredEncoder(pictureChosen);
        } else {
            throw new SteganoEncodeException("No algorithm has been selected");
        }

        Bitmap encodedBitmap = (Bitmap) encoder.encode(messageToEncode);

        String imageName = "STEGANO_" + Utils.getCurrentTimeStamp() + ".png";
        FileOutputStream out = null;
        File imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imageName);

        try {
            out = new FileOutputStream(imageFile);
            encodedBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            throw new SteganoEncodeException(e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
