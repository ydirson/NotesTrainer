package ydirson.notestrainer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.robobunny.SeekBarPreference;
import ydirson.notestrainer.Globals;

public class NoteSeekBarPreference extends SeekBarPreference {
    // dummy ctor to prevent generation of a zero-arg ctor
    public NoteSeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void updateStatusText(TextView statusText, int value) {
        if (Globals.displayedNoteNames == null) return;
        statusText.setText(Globals.displayedNoteNames[value % 7]);
    }
}
