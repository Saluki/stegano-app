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
 * Application-wide utility methods and constants that makes development easier.
 */
public class Utils {

    /**
     * A constant representing the preferences key
     */
    public static final String PREFS_NAME = "Preferences";

    /**
     * A constant representing the intent image type
     */
    public static final String INTENT_IMAGE_TYPE = "image/*";

    /**
     * The popup body padding size
     */
    private static final int BOX_PADDING = 30;

    /**
     * Build and display some fancy popup which contain the given title and text.
     *
     * @param c The current context
     * @param title The popup title, which may not be null or empty
     * @param text The popup text, which may not be null or empty
     */
    public static void buildTextViewPopUp(Context c, String title, String text) {

        if( title==null || text==null ) {
            throw new NullPointerException("Title and text may not be null");
        }

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

    /**
     * Generates a string representing the current date and time.
     *
     * @return A representation of the current date and time (YYYYMMDD_HHMMSS format)
     */
    public static String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    }

    /**
     * Changes the background color of the given layout based on shared preferences.
     *
     * @param settings The shared preferences that contains the new color configurations
     * @param layout The layout that will be impacted
     */
    public static void changeBackgroundColor(SharedPreferences settings, RelativeLayout layout) {

        int r = settings.getInt("r", 255);
        int b = settings.getInt("b", 255);
        int g = settings.getInt("g", 255);
        layout.setBackgroundColor(Color.argb(255, r, b, g));
    }
}
