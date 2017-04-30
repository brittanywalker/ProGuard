package compsci702.uoa.drinkup;

import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;

import static android.R.id.message;

public class FrontScreenActivity extends AppCompatActivity {

    public static final String MESSAGE = "compsci702.uoa.MESSAGE";
    private String m_Text = "";

    /**
    * This creates the main view
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_screen);
    }

    /** Called when the user puts in their name and presses the "ENTER" button */
    public void changeView(View view) {
        Intent intent = new Intent(this, MainWaterActivity.class);
        startActivity(intent);
    }

}
