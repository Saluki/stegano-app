package ovh.gorillahack.steganoapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ovh.gorillahack.steganoapp.R;

/**
 * Created by javi on 16.11.15.
 */
public class Utils {

    public static final String PREFS_NAME = "Preferences";
    public static final String INTENT_IMAGE_TYPE = "image/*";

    private static final int BOX_PADDING = 30;

    public static void buildTextViewPopUp(Context c, String title, String text) {

        if( title.isEmpty() || text.isEmpty() ) {
            throw new InvalidParameterException("Title and text could not be empty");
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(title);

        // Set up the input
        final TextView toShow = new TextView(c);
        toShow.setPadding(BOX_PADDING, BOX_PADDING, BOX_PADDING, BOX_PADDING);
        toShow.setText(text);
        builder.setView(toShow);

        // Set up the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    public static String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    }

    public static void changeBackgroundColor(SharedPreferences settings, RelativeLayout layout) {

        int r = settings.getInt("r", 255);
        int b = settings.getInt("b", 255);
        int g = settings.getInt("g", 255);
        layout.setBackgroundColor(Color.argb(255, r, b, g));
    }
}
