package ovh.gorillahack.steganoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ChoosePic extends AppCompatActivity {

    Button takePic;
    Button choosePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pic);

        takePic = (Button) findViewById(R.id.takePicBT);
        choosePic = (Button) findViewById(R.id.choosePicBT);
    }

    public void launchCamera(View view) {
        //TODO: lauch camera, save picture, lauch entering text to encode
    }

    public void showGallery(View view) {
        //TODO: lauch gallery and select pic from it, lauch entering text to encode
    }

}
