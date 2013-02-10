package ydirson.notestrainer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
    }

    public void runTraining(View view) {
        Intent intent = new Intent(this, ReadNotes.class);
        startActivity(intent);
    }

    public void runPrefs(View view) {
        Intent intent = new Intent(this, Prefs.class);
        startActivity(intent);
    }
}
