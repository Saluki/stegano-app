package ovh.gorillahack.steganoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import ovh.gorillahack.steganoapp.algorithm.SteganoEncoder;
import ovh.gorillahack.steganoapp.utils.Utils;

public class ChoosePic extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final int TAKE_PICTURE = 2;
    private static final String INTENT_IMAGE_TYPE = "image/*";

    String photoPath;
    String messsageToEncode = "";

    Bitmap pictureChoosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pic);
    }

    public void launchCamera(View view) {
/*
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(getApplicationContext(), R.string.error_creating_file, Toast.LENGTH_LONG).show();
                Log.e("error",ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, TAKE_PICTURE);
            }
        }
    */
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath());
        startActivityForResult(intent, TAKE_PICTURE);

    }

    public void showGallery(View view) {

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType(INTENT_IMAGE_TYPE);

        Intent chooserIntent = Intent.createChooser(getIntent(), "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            Toast.makeText(getApplicationContext(), R.string.choosepic_result_failed, Toast.LENGTH_LONG).show();
            Log.e("activity result error:", "resutl code: " + resultCode + "   request code:" + requestCode);
            return;
        }

        if (data == null || data.getData() == null) {
            Utils.buildTextViewPopUp(this, getString(R.string.error));
            Log.e("ChoosePicActivity", "Data format was null");
            return;
        }

        if (requestCode == PICK_IMAGE) {

            Uri uri = data.getData();
            try {
                this.pictureChoosen = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_LONG).show();
                Log.e("ChoosePicActivity", "Could not retrieve media: " + e.getMessage());
                return;
            }

        } else if (requestCode == TAKE_PICTURE) {

            InputStream stream = null;
            if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK)
                try {
                    // recyle unused bitmaps
                    if (pictureChoosen != null) {
                        pictureChoosen.recycle();
                    }
                    stream = getContentResolver().openInputStream(data.getData());
                    pictureChoosen = BitmapFactory.decodeStream(stream);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

        } else {
            Utils.buildTextViewPopUp(this, getString(R.string.error));
            Log.e("ChoosePicActivity", "Bad request code given: " + requestCode);
            return;
        }

        buildEditTextPopUp();
    }

    protected File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        File image = new File(storageDir, imageFileName + ".jpg");
        // Save a file: path for use with ACTION_VIEW intents
        photoPath = "file:" + image.getAbsolutePath();
        return image;

    }

    //https://stackoverflow.com/questions/10903754/input-text-dialog-android
    public void buildEditTextPopUp() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.messageToEnc);

        // Set up the input
        final EditText input = new EditText(this);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton(R.string.encode, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                messsageToEncode = input.getText().toString();
                SteganoEncoder encoder = new SteganoEncoder(pictureChoosen);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                MediaStore.Images.Media.insertImage(getContentResolver(), pictureChoosen, timeStamp + ".jpg", null);
                Utils.buildTextViewPopUp(builder.getContext(), getString(R.string.image_encrypt_succ));
                try {
                    encoder.encode(messsageToEncode);
                } catch (Exception e) {
                    Utils.buildTextViewPopUp(builder.getContext(), getString(R.string.error) + e.getMessage());
                }
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


}
