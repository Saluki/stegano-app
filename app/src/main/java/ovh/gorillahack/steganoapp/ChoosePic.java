package ovh.gorillahack.steganoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class ChoosePic extends AppCompatActivity {
    private int PICK_IMAGE = 1;
    private int TAKE_PICTURE = 2;

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
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, TAKE_PICTURE);
        }

    }

    public void showGallery(View view) {
        //lauch gallery
        Intent intent = new Intent();
        intent.setType("image/*");//only images, no videos
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                picChosen = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                //   ImageView imageView = (ImageView) findViewById(R.id.imageView);
                //   imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //TODO: come back from camera
        } else {
            //TODO:show error
        }
    }

}
