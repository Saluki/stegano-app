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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ovh.gorillahack.steganoapp.algorithm.SteganoEncoder;
import ovh.gorillahack.steganoapp.utils.Utils;

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
        if (takePictureIntent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(view.getContext(), R.string.choosepic_camera_not_found, Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the file where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), R.string.choosepic_picture_not_created, Toast.LENGTH_SHORT).show();
            Log.e("ChoosePicActivity", "Error occurred while creating file: "+e.getMessage());
            return;
        }

        // Prepare and start camera intent
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        startActivityForResult(takePictureIntent, TAKE_PICTURE);
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

        if( resultCode!=RESULT_OK ) {
            Toast.makeText(getApplicationContext(), R.string.choosepic_result_failed, Toast.LENGTH_LONG).show();
            return;
        }

        if( data==null || data.getData()==null ) {
            Utils.buildTextViewPopUp(this, getString(R.string.error));
            return;
        }

        if (requestCode == PICK_IMAGE) {

            Uri uri = data.getData();
            try {
                this.picChosen = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_LONG).show();
                Log.e("ChoosePicActivity", "Could not retrieve media: "+e.getMessage());
                return;
            }

        } else if (requestCode == TAKE_PICTURE) {

            //do when want the gallery to know the picture is there? then we need to add code https://developer.android.com/training/camera/photobasics.html
            //TODO: come back from camera and put picture in picChosen

        } else {
            Utils.buildTextViewPopUp(this, getString(R.string.error));
            return;
        }

        buildEditTextPopUp();
    }

    protected File createImageFile() throws IOException {
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
                SteganoEncoder encoder = new SteganoEncoder(picChosen);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                MediaStore.Images.Media.insertImage(getContentResolver(), picChosen, timeStamp + ".jpg", null);
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
