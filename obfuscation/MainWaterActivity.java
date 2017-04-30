package compsci702.uoa.drinkup;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Interpolator;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.CountDownTimer;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.content.Context;

import com.bumptech.glide.Glide;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MainWaterActivity extends AppCompatActivity {
    // The number of water level images
    private final double numberOfImages = 8;

    // Create a user profile object for the current session
    private UserProfile currentUser = new UserProfile();

    // Editor declaration for editing and updating the name entered in the popup
    SharedPreferences.Editor edit;

    //Sensor related
    private ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Vibrator vibrator;

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_water_screen);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(FrontScreenActivity.MESSAGE);

        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);

        //Update the variables from the saved variables and set up the UI
        updateWaterVariablesOnStart();
        setUpUI();

        // If the name is still the default name - pop up to ask the user to enter their name
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
        String currentName = p.getString("name", "John Smith");
        if (currentName.equals("John Smith")){
            initialPopUp();
        }

        //Inital Call to start lowering the water level.
        updateFullnessLevel();
        updateWaterLevelImage();

        // Create the count down timer for lowering the water level over time.
        // Countdown timer is started onResume and Canceled onPause
        countdownWaterLevel();

        //sensor
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(mAccelerometer != null){
            if(mShakeDetector == null){
                mShakeDetector = new ShakeDetector();
            }
            mShakeDetector.setShakeListener(new ShakeListener() {
                @Override
                public void onShake() {
                    handleShakeEvent();
                }
            });
        }
        else{
            Log.d("DrinkUp","Accelerometer is not available. Shake won't trigger the add glass.");
        }
        // Get instance of Vibrator from current Context
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    /**
     * This method creates a popup to ask for user name on the very first launch or if the name has not been set.
     */
    public void initialPopUp(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter your name to get started:");

        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // get the shared preferences manager
        edit = PreferenceManager.getDefaultSharedPreferences(this).edit();

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // take the name the user entered and save it as the users name in preferences
                String nameEntered = input.getText().toString();
                edit.putString("name",nameEntered);
                edit.apply();
            }
        });
        // create cancel functionality
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    /**
     * This method sets up the UI on create
     * It sets the values of the text and starts the animations.
     */
    public void setUpUI(){

        // set the text of the number of glasses consumed
        TextView glasses_view = (TextView) findViewById(R.id.number_of_glasses);
        int glassestoGo = max((int)currentUser.getGoalGlasses() - currentUser.getNumberOfGlassesConsumed(), 0);
        glasses_view.setText(String.valueOf(glassestoGo));

        // Load the ImageView that will host the animation and
        // set its background to our AnimationDrawable XML resource.
        ImageView img = (ImageView)findViewById(R.id.goldfish_view);
        img.setBackgroundResource(R.drawable.swimanimation);
        // Get the background, which has been compiled to an AnimationDrawable object.
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        // Start the animation (looped playback by default).
        frameAnimation.start();

    }

    /**
     * Method to update the class variables - goal water level and current glasses consumed.
     * This method reads from the shared prefs
     */
    public void updateWaterVariablesOnStart(){

        // Get the user preferences
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);

        // calculate and update the goal and water values for the user profile object
        double startHourPrefVal = Double.parseDouble(p.getString("goal_start_hour", getResources().getString(R.string.goal_start_hour_default)));
        currentUser.setStartHour(startHourPrefVal);

        double endHourPrefVal = Double.parseDouble(p.getString("goal_end_hour", getResources().getString(R.string.goal_end_hour_default)));
        currentUser.setCountdownHourLength((endHourPrefVal - startHourPrefVal) + 1);

        String goal_glasses = p.getString("goal_glasses",getResources().getString(R.string.glasses_number_default));
        currentUser.setGoalGlasses(Double.parseDouble(goal_glasses));
        currentUser.setInitialFullnessLevel(currentUser.getGoalGlasses());
        currentUser.setGlassesPerHourGoal(currentUser.getGoalGlasses()/currentUser.getCountdownHourLength());

        // calculate the number of glasses consumed and if the user has consumed any glasses today
        SharedPreferences mPrefs = getSharedPreferences("numberOfGlassesConsumed", 0);
        String mString = mPrefs.getString("numberOfGlassesConsumed", "0");

        // check the date against today to see if the user has had any water today
        SharedPreferences datePrefs = getSharedPreferences("mostRecentDate", 0);
        String date = datePrefs.getString("mostRecentDate", "0");

        //get the current date
        Calendar c = Calendar.getInstance();
        String currentDate = Integer.toString(c.get(Calendar.DAY_OF_MONTH)) + Integer.toString(c.get(Calendar.MONTH)) + Integer.toString(c.get(Calendar.YEAR));

        // If the most recent saved date was today - then get the number of glasses consumed today
        if (date.equals(currentDate)) {
            currentUser.setNumberOfGlassesConsumed(Integer.parseInt(mString));
        }
        // Otherwise reset the number of glasses to be zero - as is a new day
        else {
            currentUser.setNumberOfGlassesConsumed(0);
        }
        // Save the updated number of glasses value
        SharedPreferences y = getSharedPreferences("numberOfGlassesConsumed", 0);
        SharedPreferences.Editor mEditor = y.edit();
        mEditor.putString("numberOfGlassesConsumed", Integer.toString(currentUser.getNumberOfGlassesConsumed())).apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        Glide.with(this).resumeRequests();
        // Call the count down timer to start lowering the water level over time.
        timer.start();
        Log.d("DrinkUp", "Main Activity onResume!");
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        timer.cancel();
        Glide.with(this).pauseRequests();
        Log.d("DrinkUp", "Main Activity onPause!");
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        Log.d("DrinkUp", "Main Activity onDestroy!");
        super.onDestroy();
    }

    /**
    * This is called when this intent is created, and adds the settings option to the action bar
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    /**
    * This called by the action bar
    * the "openSettings" function if the action is to open the settings menu
    * otherwise the default superclass is called
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_settings was selected
            case R.id.action_settings:
                openSettings();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void handleShakeEvent(){
        ImageButton add_glass_btn = (ImageButton) findViewById(R.id.button2);
        add_glass_btn.performClick();
        if(vibrator != null){
            vibrator.vibrate(200);
        }
    }

    /**
     * Called when the user presses the add a glass of water button to increment the total number of glasses
     * This method adds one to the glasses consumed and calls methods to update the UI
     */
    public void increment_water(View view) {

        // Get the number of glasses the user has currently had and increment by 1.
        TextView glasses_view = (TextView) findViewById(R.id.number_of_glasses);

        // Increment the fullness level and call the update image function to update the water level image
        SharedPreferences prefs = getSharedPreferences("numberOfGlassesConsumed", 0);
        int prevGlasses = Integer.parseInt(prefs.getString("numberOfGlassesConsumed", "0"));
        currentUser.setNumberOfGlassesConsumed (prevGlasses + 1);

        // Set the text and progressbar to update to the incremented value of water.
        int glassestoGo = max((int)currentUser.getGoalGlasses() - currentUser.getNumberOfGlassesConsumed(), 0);
        glasses_view.setText(String.valueOf(glassestoGo));

        // Save the new value of numberOfGlassesConsumed to the sharedprefs value
        SharedPreferences mPrefs = getSharedPreferences("numberOfGlassesConsumed", 0);
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString("numberOfGlassesConsumed", Integer.toString(currentUser.getNumberOfGlassesConsumed())).apply();

        // Save the current date to keep track of when the user last used the application
        SharedPreferences datePrefs = getSharedPreferences("mostRecentDate", 0);
        SharedPreferences.Editor dateEditor = datePrefs.edit();
        Calendar c = Calendar.getInstance();
        String currentDate = Integer.toString(c.get(Calendar.DAY_OF_MONTH)) + Integer.toString(c.get(Calendar.MONTH)) + Integer.toString(c.get(Calendar.YEAR));
        dateEditor.putString("mostRecentDate", currentDate).commit();

        // Call functions to update the water level image and recalculate water level
        updateFullnessLevel();
        updateWaterLevelImage();

    }

    /**
     * This method starts the countdown timer to control the water level of the fish bowl.
     */
    public void countdownWaterLevel(){
        // Create a new countdown timer to check the time and update the GUI. (Currently every 5 mins)
        timer = new CountDownTimer(100000000, 300000) {
            // This method decreasesr the fullness level by one - on tick executes once every 10 seconds.
            public void onTick(long millisUntilFinished) {
                // Calculate the new level of the water and update the water level image
                Log.d("DrinkUp", "CountDownTimer On Tick!");
                updateFullnessLevel();
                updateWaterLevelImage();
            }
            public void onFinish() {
                Log.d("DrinkUp", "CountDownTimer OnFinished!");
            }
        };
    }

    /**
     * This method updates the field fullness level and calculate the level of the water of the fish tank
     */
    public void updateFullnessLevel(){

        // Get the current time from the calendar and calculate the number of minutes passed since the start of measuring water
        Calendar c = Calendar.getInstance();
        int currentHour = c.get(Calendar.HOUR_OF_DAY);
        int currentMinute = c.get(Calendar.MINUTE);
        currentUser.setTimePassed( (currentHour - currentUser.getStartHour()) * 60 + currentMinute);
        currentUser.setTimePassed(min(currentUser.getTimePassed(), 720));

        // Calculate the fullness level by subtracting the number of glasses per hour goal by the number of hours passed, from the initial fullness level (100%).
        double waterLevel = currentUser.getInitialFullnessLevel() - (currentUser.getTimePassed() * currentUser.getGlassesPerHourGoal()) / 60;
        //Normalise the water level to be a fraction of the 8 water level images
        waterLevel = (waterLevel / currentUser.getGoalGlasses()) * numberOfImages;
        // Rounding up always to remain at the more full image until the threshold has been passed to swap images.
        currentUser.setFullnessLevel(Math.ceil(waterLevel));

        //Add on the number of glasses consumed today by the user.
        currentUser.setFullnessLevel( currentUser.getFullnessLevel() + Math.floor(((double)currentUser.getNumberOfGlassesConsumed()) * numberOfImages / currentUser.getGoalGlasses()));

    }

    /**
     * This method updates the water level image based on the fullnesslevel
     */
    public void updateWaterLevelImage(){
        Log.d("DrinkUp", " WaterLevel:" + Double.toString(currentUser.getFullnessLevel()));
        // If the fullness level is over 8 glasses it is full
        // Otherwise set it to be the appropriate level of empty
        if (currentUser.getFullnessLevel() >= 8) {
            ImageView fish_bowl_view = (ImageView) findViewById(R.id.fishBowlView);
            Glide.with(this).load(R.drawable.fishbowl8).into(fish_bowl_view);
        }
        else if (currentUser.getFullnessLevel() == 7) {
            ImageView fish_bowl_view = (ImageView) findViewById(R.id.fishBowlView);
            Glide.with(this).load(R.drawable.fishbowl7).into(fish_bowl_view);
        }
        else if (currentUser.getFullnessLevel() == 6) {
            ImageView fish_bowl_view = (ImageView) findViewById(R.id.fishBowlView);
            Glide.with(this).load(R.drawable.fishbowl6).into(fish_bowl_view);
        }
        else if (currentUser.getFullnessLevel() == 5) {
            ImageView fish_bowl_view = (ImageView) findViewById(R.id.fishBowlView);
            Glide.with(this).load(R.drawable.fishbowl5).into(fish_bowl_view);
        }
        else if (currentUser.getFullnessLevel() == 4) {
            ImageView fish_bowl_view = (ImageView) findViewById(R.id.fishBowlView);
            Glide.with(this).load(R.drawable.fishbowl4).into(fish_bowl_view);
        }
        else if (currentUser.getFullnessLevel() == 3) {
            ImageView fish_bowl_view = (ImageView) findViewById(R.id.fishBowlView);
            Glide.with(this).load(R.drawable.fishbowl3).into(fish_bowl_view);
        }
        else if (currentUser.getFullnessLevel() == 2) {
            ImageView fish_bowl_view = (ImageView) findViewById(R.id.fishBowlView);
            Glide.with(this).load(R.drawable.fishbowl2).into(fish_bowl_view);
        }
        // if the tank has been re-filled from empty start the animation again.
        else if (currentUser.getFullnessLevel() == 1) {
            ImageView fish_bowl_view = (ImageView) findViewById(R.id.fishBowlView);
            Glide.with(this).load(R.drawable.fishbowl1).into(fish_bowl_view);

            ImageView fish_view = (ImageView)findViewById(R.id.goldfish_view);
            fish_view.setVisibility(View.VISIBLE);

            ImageView deadfish_view = (ImageView) findViewById(R.id.deadfish_view);
            deadfish_view.setVisibility(View.GONE);
        }
        // If the tank is empty switch to dead fish image
        else if (currentUser.getFullnessLevel() == 0) {

            ImageView fish_bowl_view = (ImageView) findViewById(R.id.fishBowlView );
            Glide.with(this).load(R.drawable.fishbowl0).into(fish_bowl_view);

            ImageView fish_view = (ImageView)findViewById(R.id.goldfish_view);
            fish_view.setVisibility(View.GONE);

            ImageView deadfish_view = (ImageView) findViewById(R.id.deadfish_view);
            deadfish_view.setVisibility(View.VISIBLE);
        }
    }

    /**
    * This function opens the settings menu
    */
    public void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}
