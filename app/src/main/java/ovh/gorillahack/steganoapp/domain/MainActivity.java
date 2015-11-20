package ovh.gorillahack.steganoapp.domain;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import ovh.gorillahack.steganoapp.R;
import ovh.gorillahack.steganoapp.utils.Utils;

public class MainActivity extends AppCompatActivity {

    //TODO: add localisation (values/strings en plusieurs langues(FR-EN))
    //TODO: check passing variables between activities (e.g. PREFS_NAME)
    //TODO: settings reworking (Javier is on it)
    //TODO: IF we have time, when taking picture, work with full pic and not with thumbnail


    RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_main_layout);
    public static final String PREFS_NAME = "Preferences";

    Button encode;
    Button decode;
    Button info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        int r = settings.getInt("r", 0);
        int b = settings.getInt("b", 0);
        int g = settings.getInt("g", 0);
        layout.setBackgroundColor(Color.argb(255, r, b, g));

        encode = (Button) findViewById(R.id.encodeBT);
        decode = (Button) findViewById(R.id.decodeBT);
        info = (Button) findViewById(R.id.infoBT);


    }

    public void launchEncodeActivity(View v) {
        Intent intent = new Intent(this, ChoosePic.class);
        startActivity(intent);
    }

    public void lauchDecodeActivity(View v) {
        Intent intent = new Intent(this, DecodeActivity.class);
        startActivity(intent);
    }

    public void launchSettingsActivity(View v) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void showInfo(View v) {
        Utils.buildTextViewPopUp(this, getString(R.string.informations));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
