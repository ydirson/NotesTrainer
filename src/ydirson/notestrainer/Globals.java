package ydirson.notestrainer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Globals {
    static String[] displayedNoteNames;

    // constants
    public static final String[] noteNames =
        new String[] { "A", "B", "C", "D", "E", "F", "G" };

    public static final String[] noteNames_latin =
        new String[] { "la", "si", "do", "re", "mi", "fa", "sol" };
    public static final String[] noteNames_german =
        new String[] { "A", "H", "C", "D", "E", "F", "G" };

    public static void init(Context context) {
        // notation to use
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String notation = sharedPrefs.getString("pref_notation", "english");
        if (notation.equals("english")) Globals.displayedNoteNames = noteNames;
        else if (notation.equals("latin")) Globals.displayedNoteNames = noteNames_latin;
        else if (notation.equals("german")) Globals.displayedNoteNames = noteNames_german;
        // else FIXME
    }
}
