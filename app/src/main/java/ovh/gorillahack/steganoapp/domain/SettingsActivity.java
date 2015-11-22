package ovh.gorillahack.steganoapp.domain;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import ovh.gorillahack.steganoapp.R;
import ovh.gorillahack.steganoapp.utils.Utils;

public class SettingsActivity extends AppCompatActivity {
    int r, b, g, oldR, oldB, oldG;

    SeekBar greenSeekBar;
    SeekBar redSeekBar;
    SeekBar blueSeekBar;

    TextView greenValue;
    TextView redValue;
    TextView yelowValue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        greenSeekBar = (SeekBar) findViewById(R.id.greenSeekBar);
        redSeekBar = (SeekBar) findViewById(R.id.redSeekBar);
        blueSeekBar = (SeekBar) findViewById(R.id.blueSeekBar);

        greenValue = (TextView) findViewById(R.id.greenTV);
        redValue = (TextView) findViewById(R.id.redTV);
        yelowValue = (TextView) findViewById(R.id.blueTV);

        SharedPreferences settings = getSharedPreferences(Utils.PREFS_NAME, 0);

        oldR = r = settings.getInt("r", 255);
        oldB = b = settings.getInt("b", 255);
        oldG = g = settings.getInt("g", 255);

        refresh();

        greenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                greenValue.setText(String.valueOf(progress));
                b = progress;
                changeBackroundColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        redSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                redValue.setText(String.valueOf(progress));
                r = progress;
                changeBackroundColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        blueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                yelowValue.setText(String.valueOf(progress));
                g = progress;
                changeBackroundColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveColors();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveColors();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveColors();
    }

    private void saveColors() {
        SharedPreferences.Editor e = getSharedPreferences(Utils.PREFS_NAME, 0).edit();
        e.putInt("r", r);
        e.putInt("b", b);
        e.putInt("g", g);
        e.commit();
    }

    public void setToWhite(View v) {
        oldR = r;
        oldB = b;
        oldG = g;
        r = b = g = 255;
        refresh();
    }

    public void resetToPreviousColor(View v) {
        r = oldR;
        b = oldB;
        g = oldG;
        refresh();
    }

    private void refresh() {
        changeBackroundColor();
        redValue.setText(String.valueOf(r));
        redSeekBar.setProgress(r);
        greenValue.setText(String.valueOf(b));
        greenSeekBar.setProgress(b);
        yelowValue.setText(String.valueOf(g));
        blueSeekBar.setProgress(g);
    }


    private void changeBackroundColor() {
        findViewById(R.id.activity_settings_layout).setBackgroundColor(Color.argb(255, r, b, g));
    }
}
