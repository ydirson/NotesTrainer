package ydirson.notestrainer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import ydirson.notestrainer.Globals;

public class MainMenu extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Globals.init(this);
        setContentView(R.layout.mainmenu);
    }

    public void runTraining(View view) {
        Intent intent = new Intent(this, ReadNotes.class);
        intent.putExtra(ReadNotes.EXTRA_MODE, ReadNotes.GameMode.TRAINING);
        startActivity(intent);
    }

    public void runOneMinute(View view) {
        Intent intent = new Intent(this, ReadNotes.class);
        intent.putExtra(ReadNotes.EXTRA_MODE, ReadNotes.GameMode.ONEMINUTE);
        startActivity(intent);
    }

    public void runPrefs(View view) {
        Intent intent = new Intent(this, Prefs.class);
        startActivity(intent);
    }
}
