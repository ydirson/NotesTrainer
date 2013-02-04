package ydirson.notestrainer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import java.util.Random;
import ydirson.notestrainer.ScoreView;

public class ReadNotes extends Activity {
    boolean _started = false;
    ScoreView _scoreview;
    Random _rng;
    int _currentNote = -1;
    Chronometer _chrono;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        LinearLayout score = (LinearLayout) findViewById(R.id.score);
        _scoreview = new ScoreView(this);
        score.addView(_scoreview);

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
}
