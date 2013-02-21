package ydirson.notestrainer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import ydirson.notestrainer.Globals;
import ydirson.notestrainer.ScoreView;

public class ReadNotes extends Activity {
    boolean _started;
    ScoreView _scoreview;
    Random _rng;
    int _currentNote = -1;
    SharedPreferences _sharedPrefs;

    Chronometer _chrono;
    long _elapsedTime; // keep track of elapsed time when paused, -1 when not paused

    // tunable params
    int _noteMin, _noteMax;

    // constants
    List<String> noteNamesList = Arrays.asList(Globals.noteNames);

    final String TAGPREFIX = "note_";

    // game type
    public enum GameMode { TRAINING, ONEMINUTE };
    GameMode _gameMode;
    // intent extra id for mode
    public final static String EXTRA_MODE = "ydirson.notestrainer.GameMode";

    // scoring
    int _nPresented, _nCorrect, _nIncorrect;

    // keys for saved state
    static final String STATE_STARTED = "started";
    static final String STATE_CURRENTNOTE = "currentNote";
    static final String STATE_ELAPSEDTIME = "elapsedTime";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        // create the score widget
        LinearLayout score = (LinearLayout) findViewById(R.id.score);
        _scoreview = new ScoreView(this);
        score.addView(_scoreview);

        // preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        _sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // remainder
        _chrono = (Chronometer) findViewById(R.id.chrono);
        _chrono.setOnChronometerTickListener(chronometerTickListener);
        _rng = new Random();

        // initialize game state
        _gameMode = (GameMode)getIntent().getSerializableExtra(EXTRA_MODE);
        if (savedInstanceState != null) {
            _started = savedInstanceState.getBoolean(STATE_STARTED);
            _elapsedTime = savedInstanceState.getLong(STATE_ELAPSEDTIME);
            if (_started) {
                _currentNote = savedInstanceState.getInt(STATE_CURRENTNOTE);
                _scoreview.setNote(_currentNote);
            }
        } else {
            _started = false;
            _elapsedTime = -1;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Separate from onCreate to force update when we get back
        // from the preference Activity
        Globals.init(this);
        setupNoteButtons();

        String clef = _sharedPrefs.getString("pref_clef", "g2");
        if (clef.equals("g2")) _scoreview.setClef(ScoreView.ClefG2);
        else if (clef.equals("f4")) _scoreview.setClef(ScoreView.ClefF4);
        else if (clef.equals("c3")) _scoreview.setClef(ScoreView.ClefC3);
        else if (clef.equals("c4")) _scoreview.setClef(ScoreView.ClefC4);
        // else FIXME

        _noteMin = _sharedPrefs.getInt("pref_minnote", 14); // A3
        _noteMax = _sharedPrefs.getInt("pref_maxnote", 30); // C5
        // force range validity
        if (_noteMax < _noteMin)
            _noteMax = _noteMin + 1;
    }

    @Override
    public void onPause() {
        if (_started) {
            _chrono.stop();
            _elapsedTime = SystemClock.elapsedRealtime() - _chrono.getBase();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (_started) {
            _chrono.setBase(SystemClock.elapsedRealtime() - _elapsedTime);
            _chrono.start();
            _elapsedTime = -1;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putBoolean(STATE_STARTED, _started);
        savedInstanceState.putInt(STATE_CURRENTNOTE, _currentNote);
        savedInstanceState.putLong(STATE_ELAPSEDTIME,
                                   SystemClock.elapsedRealtime() - _chrono.getBase());

        // Save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    int _randomNote() {
        int note;
        do {
            note = _noteMin + _rng.nextInt(_noteMax - _noteMin + 1);
        } while (note == _currentNote);
        _nPresented ++;
        return note;
    }

    public void onStartStop(View view) {
        if (!_started) {
            _nPresented = 0; _nCorrect = 0; _nIncorrect = 0;
            _currentNote = _randomNote();
            _started = true;
            _chrono.setBase(SystemClock.elapsedRealtime());
            _chrono.start();
        } else {
            _currentNote = -1;
            _started = false;
            _chrono.stop();
            long nSeconds = (SystemClock.elapsedRealtime() - _chrono.getBase()) / 1000;
            Toast.makeText(this, String.format("%d in %d seconds\n%d errors",
                                               _nCorrect, nSeconds,
                                               _nIncorrect), Toast.LENGTH_SHORT).show();
        }
        _scoreview.setNote(_currentNote);
    }

    Chronometer.OnChronometerTickListener chronometerTickListener =
        new Chronometer.OnChronometerTickListener() {
            @Override
                public void onChronometerTick(Chronometer chronometer) {
                switch (_gameMode) {
                case TRAINING:
                    break;
                case ONEMINUTE:
                    if (SystemClock.elapsedRealtime() - chronometer.getBase() >= 60 * 1000) {
                        // end the game
                        onStartStop(null);
                    }
                    break;
                default: throw new IllegalArgumentException(String.format("unsupported game mode %s",
                                                                          _gameMode));
                }
            }
        };

    public void onChooseNote(View view) {
        Button b = (Button)view;
        String tag = (String) b.getTag();
        if (! tag.startsWith(TAGPREFIX))
            // FIXME should log error
            return;
        int note_idx = noteNamesList.indexOf(tag.substring(TAGPREFIX.length()));
        if (note_idx == _currentNote % 7) {
            _currentNote = _randomNote();
            _scoreview.setNote(_currentNote);
            _nCorrect ++;
        } else
            _nIncorrect ++;
    }

    public void setupNoteButtons() {
        // note button labels
        LinearLayout main = (LinearLayout) findViewById(R.id.main);
        for (int noteIdx = 0; noteIdx < Globals.noteNames.length; noteIdx++) {
            final String tag = TAGPREFIX + Globals.noteNames[noteIdx];
            Button b = (Button) main.findViewWithTag(tag);
            if (b != null)
                b.setText(Globals.displayedNoteNames[noteIdx]);
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
