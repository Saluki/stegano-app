package ovh.gorillahack.steganoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import ovh.gorillahack.steganoapp.utils.Utils;

public class MainActivity extends AppCompatActivity {

    Button encode;
    Button decode;
    Button info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
