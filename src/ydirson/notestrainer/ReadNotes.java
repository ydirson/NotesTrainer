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

        LinearLayout main = (LinearLayout) findViewById(R.id.main);
        main.addView(new ScoreView(this));
    }
}
