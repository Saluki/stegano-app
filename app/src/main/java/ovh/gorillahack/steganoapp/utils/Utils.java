package ovh.gorillahack.steganoapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TextView;

import ovh.gorillahack.steganoapp.R;

/**
 * Created by javi on 16.11.15.
 */
public class Utils {

    public static void buildTextViewPopUp(Context c, String text) {//TODO: add title in parameters
        final AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(R.string.info);

        // Set up the input
        final TextView toShow = new TextView(c);
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
}
