package ydirson.notestrainer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import ydirson.notestrainer.ScoreView;

public class ReadNotes extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        LinearLayout score = (LinearLayout) findViewById(R.id.score);
        _scoreview = new ScoreView(this);
        score.addView(_scoreview);
    }
}
