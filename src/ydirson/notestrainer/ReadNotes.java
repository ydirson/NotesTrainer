package ydirson.notestrainer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import ydirson.notestrainer.ScoreView;

public class ReadNotes extends Activity {
    boolean _started = false;
    ScoreView _scoreview;
    Random _rng;
    int _currentNote = -1;
    Chronometer _chrono;

    List<String> noteNames =
        Arrays.asList(new String[] { "A", "B", "C", "D", "E", "F", "G" });

    final String TAGPREFIX = "note_";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        // create the score widget
        LinearLayout score = (LinearLayout) findViewById(R.id.score);
        _scoreview = new ScoreView(this);
        score.addView(_scoreview);

        // note button labels
        LinearLayout main = (LinearLayout) findViewById(R.id.main);
        for (String englishNote: noteNames) {
            final String tag = TAGPREFIX + englishNote;
            Button b = (Button) main.findViewWithTag(tag);
            if (b != null)
                b.setText(englishNote);
        }

        // remainder
        _chrono = (Chronometer) findViewById(R.id.chrono);
        _rng = new Random();
    }

    int _randomNote() {
        int note;
        do {
            // 10 = D2 - 34 = G5
            note = 10 + _rng.nextInt(24);
        } while (note == _currentNote);
        return note;
    }

    public void onStartStop(View view) {
        if (!_started) {
            _currentNote = _randomNote();
            _started = true;
            _chrono.setText("0");
            _chrono.start();
        } else {
            _currentNote = -1;
            _started = false;
            _chrono.stop();
        }
        _scoreview.setNote(_currentNote);
    }

    public void onChooseNote(View view) {
        Button b = (Button)view;
        String tag = (String) b.getTag();
        if (! tag.startsWith(TAGPREFIX))
            // FIXME should log error
            return;
        int note_idx = noteNames.indexOf(tag.substring(TAGPREFIX.length()));
        if (note_idx == _currentNote % 7) {
            _currentNote = _randomNote();
            _scoreview.setNote(_currentNote);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, Prefs.class);
        startActivity(intent);
        return true; // consumed
    }
}
