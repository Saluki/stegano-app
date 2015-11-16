package ovh.gorillahack.steganoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ovh.gorillahack.steganoapp.algorithm.SteganoEncoder;

public class ChoosePic extends AppCompatActivity {
    private int PICK_IMAGE = 1;
    private int TAKE_PICTURE = 2;
    String photoPath;
    String messsageToEncode = "";

    Button takePic;
    Button choosePic;

    Bitmap picChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pic);

        takePic = (Button) findViewById(R.id.takePicBT);
        choosePic = (Button) findViewById(R.id.choosePicBT);
    }

    public void launchCamera(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                //TODO: gerer exception
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, TAKE_PICTURE);
            }
        }
    }

    public void showGallery(View view) {
        //launch gallery
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");//only images, no videos


        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent(), "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                picChosen = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //do when want the gallery to know the picture is there? then we need to add code https://developer.android.com/training/camera/photobasics.html
            //TODO: come back from camera and put picture in picChosen
        } else {
            buildTextViewPopUp(getString(R.string.error));
        }
        buildEditTextPopUp();

    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(timeStamp, ".jpg", storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        photoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    //https://stackoverflow.com/questions/10903754/input-text-dialog-android
    public void buildEditTextPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.messageToEnc);

        // Set up the input
        final EditText input = new EditText(this);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton(R.string.encode, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                messsageToEncode = input.getText().toString();
                SteganoEncoder encoder = new SteganoEncoder(picChosen);
                buildTextViewPopUp(getString(R.string.image_encrypt_succ));
                try {
                    encoder.encode(messsageToEncode);
                } catch (Exception e) {
                    buildTextViewPopUp(getString(R.string.error) + e.getMessage());
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

    public void buildTextViewPopUp(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.informations);

        // Set up the input
        TextView toShow = new TextView(this);
        toShow.setText(text);
        builder.setView(toShow);

        builder.show();
    }
}
