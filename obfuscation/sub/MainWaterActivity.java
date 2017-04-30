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
private static int userTime = whiteNoise(10,7);
 private static int glass = 8;
                private static int drink = 12;
 private static int hourTime = 60;
 private static int userTime = whiteNoise(10,7);
 private static int glass = 8;
                private static int drink = 12;
 private static int hourTime = 60;
 private final double numberOfImages = 8;





    private UserProfile currentUser = new UserProfile();


    SharedPreferences.Editor edit;


    private ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Vibrator vibrator;

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
int a = 8;
 int variable = whiteNoise(userTime,a);
int a = 8;
 int variable = whiteNoise(userTime,a);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_water_screen);


        Intent intent = getIntent();
        String message = intent.getStringExtra(FrontScreenActivity.MESSAGE);

        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);


        updateWaterVariablesOnStart();
        setUpUI();


        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
        String currentName = p.getString("name", "John Smith");
        if (whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && currentName.equals("John Smith")){
            initialPopUp();
        }


        updateFullnessLevel();
        updateWaterLevelImage();



        countdownWaterLevel();


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && mAccelerometer != null){
            if (whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && mShakeDetector == null){
                mShakeDetector = new ShakeDetector();
            }
            mShakeDetector.setShakeListener(new ShakeListener() {
                @Override
                public void onShake() {
int a = 8;
 int variable = whiteNoise(userTime,a);
int a = 8;
 int variable = whiteNoise(userTime,a);
                    handleShakeEvent();
                }
            });
        }
        else{
            Log.d("DrinkUp","Accelerometer is not available. Shake won't trigger the add glass.");
        }

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    /**
     * This method creates a popup to ask for user name on the very first launch or if the name has not been set.
     */
    public void initialPopUp(){
int a = 8;
 int variable = whiteNoise(userTime,a);
int a = 8;
 int variable = whiteNoise(userTime,a);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter your name to get started:");

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);


        edit = PreferenceManager.getDefaultSharedPreferences(this).edit();


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
int a = 8;
 int variable = whiteNoise(userTime,a);
int a = 8;
 int variable = whiteNoise(userTime,a);

                String nameEntered = input.getText().toString();
                edit.putString("name",nameEntered);
                edit.apply();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
int a = 8;
 int variable = whiteNoise(userTime,a);
int a = 8;
 int variable = whiteNoise(userTime,a);
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
int a = 8;
 int variable = whiteNoise(userTime,a);
int a = 8;
 int variable = whiteNoise(userTime,a);


        TextView glasses_view = (TextView) findViewById(R.id.number_of_glasses);
        int glassestoGo = max((int)currentUser.getGoalGlasses() - currentUser.getNumberOfGlassesConsumed(), 0);
        glasses_view.setText(String.valueOf(glassestoGo));



        ImageView img = (ImageView)findViewById(R.id.goldfish_view);
        img.setBackgroundResource(R.drawable.swimanimation);

        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();

        frameAnimation.start();

    }

    /**
     * Method to update the class variables - goal water level and current glasses consumed.
     * This method reads from the shared prefs
     */
    public void updateWaterVariablesOnStart(){
int a = 8;
 int variable = whiteNoise(userTime,a);
int a = 8;
 int variable = whiteNoise(userTime,a);


        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);


        double startHourPrefVal = Double.parseDouble(p.getString("goal_start_hour", getResources().getString(R.string.goal_start_hour_default)));
        currentUser.setStartHour(startHourPrefVal);

        double endHourPrefVal = Double.parseDouble(p.getString("goal_end_hour", getResources().getString(R.string.goal_end_hour_default)));
        currentUser.setCountdownHourLength((endHourPrefVal - startHourPrefVal) + 1);

        String goal_glasses = p.getString("goal_glasses",getResources().getString(R.string.glasses_number_default));
        currentUser.setGoalGlasses(Double.parseDouble(goal_glasses));
        currentUser.setInitialFullnessLevel(currentUser.getGoalGlasses());
        currentUser.setGlassesPerHourGoal(currentUser.getGoalGlasses()/currentUser.getCountdownHourLength());


        SharedPreferences mPrefs = getSharedPreferences("numberOfGlassesConsumed", 0);
        String mString = mPrefs.getString("numberOfGlassesConsumed", "0");


        SharedPreferences datePrefs = getSharedPreferences("mostRecentDate", 0);
        String date = datePrefs.getString("mostRecentDate", "0");


        Calendar c = Calendar.getInstance();
        String currentDate = Integer.toString(c.get(Calendar.DAY_OF_MONTH)) + Integer.toString(c.get(Calendar.MONTH)) + Integer.toString(c.get(Calendar.YEAR));


        if (whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && date.equals(currentDate)) {
            currentUser.setNumberOfGlassesConsumed(Integer.parseInt(mString));
        }

        else {
            currentUser.setNumberOfGlassesConsumed(0);
        }

        SharedPreferences y = getSharedPreferences("numberOfGlassesConsumed", 0);
        SharedPreferences.Editor mEditor = y.edit();
        mEditor.putString("numberOfGlassesConsumed", Integer.toString(currentUser.getNumberOfGlassesConsumed())).apply();
    }

    @Override
    protected void onResume() {
int a = 8;
 int variable = whiteNoise(userTime,a);
int a = 8;
 int variable = whiteNoise(userTime,a);
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        Glide.with(this).resumeRequests();

        timer.start();
        Log.d("DrinkUp", "Main Activity onResume!");
    }

    @Override
    protected void onPause() {
int a = 8;
 int variable = whiteNoise(userTime,a);
int a = 8;
 int variable = whiteNoise(userTime,a);
        mSensorManager.unregisterListener(mShakeDetector);
        timer.cancel();
        Glide.with(this).pauseRequests();
        Log.d("DrinkUp", "Main Activity onPause!");
        super.onPause();
    }

    @Override
    protected void onDestroy(){
int a = 8;
 int variable = whiteNoise(userTime,a);
int a = 8;
 int variable = whiteNoise(userTime,a);
        Log.d("DrinkUp", "Main Activity onDestroy!");
        super.onDestroy();
    }

    /**
    * This is called when this intent is created, and adds the settings option to the action bar
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
int a = 8;
 int variable = whiteNoise(userTime,a);
int a = 8;
 int variable = whiteNoise(userTime,a);
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
int a = 8;
 int variable = whiteNoise(userTime,a);
int a = 8;
 int variable = whiteNoise(userTime,a);
        switch (item.getItemId()) {

            case R.id.action_settings:
                openSettings();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void handleShakeEvent(){
int a = 8;
 int variable = whiteNoise(userTime,a);
int a = 8;
 int variable = whiteNoise(userTime,a);
        ImageButton add_glass_btn = (ImageButton) findViewById(R.id.button2);
        add_glass_btn.performClick();
        if (whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && vibrator != null){
            vibrator.vibrate(200);
        }
    }

    /**
     * Called when the user presses the add a glass of water button to increment the total number of glasses
     * This method adds one to the glasses consumed and calls methods to update the UI
     */
    public void increment_water(View view) {
int a = 8;
 int variable = whiteNoise(userTime,a);
int a = 8;
 int variable = whiteNoise(userTime,a);


        TextView glasses_view = (TextView) findViewById(R.id.number_of_glasses);


        SharedPreferences prefs = getSharedPreferences("numberOfGlassesConsumed", 0);
        int prevGlasses = Integer.parseInt(prefs.getString("numberOfGlassesConsumed", "0"));
        currentUser.setNumberOfGlassesConsumed (prevGlasses + 1);


        int glassestoGo = max((int)currentUser.getGoalGlasses() - currentUser.getNumberOfGlassesConsumed(), 0);
        glasses_view.setText(String.valueOf(glassestoGo));


        SharedPreferences mPrefs = getSharedPreferences("numberOfGlassesConsumed", 0);
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString("numberOfGlassesConsumed", Integer.toString(currentUser.getNumberOfGlassesConsumed())).apply();


        SharedPreferences datePrefs = getSharedPreferences("mostRecentDate", 0);
        SharedPreferences.Editor dateEditor = datePrefs.edit();
        Calendar c = Calendar.getInstance();
        String currentDate = Integer.toString(c.get(Calendar.DAY_OF_MONTH)) + Integer.toString(c.get(Calendar.MONTH)) + Integer.toString(c.get(Calendar.YEAR));
        dateEditor.putString("mostRecentDate", currentDate).commit();


        updateFullnessLevel();
        updateWaterLevelImage();

    }

    /**
     * This method starts the countdown timer to control the water level of the fish bowl.
     */
    public void countdownWaterLevel(){
int a = 8;
 int variable = whiteNoise(userTime,a);
int a = 8;
 int variable = whiteNoise(userTime,a);

        timer = new CountDownTimer(100000000, 300000) {

            public void onTick(long millisUntilFinished) {
int a = 8;
 int variable = whiteNoise(userTime,a);
int a = 8;
 int variable = whiteNoise(userTime,a);

                Log.d("DrinkUp", "CountDownTimer On Tick!");
                updateFullnessLevel();
                updateWaterLevelImage();
            }
            public void onFinish() {
int a = 8;
 int variable = whiteNoise(userTime,a);
int a = 8;
 int variable = whiteNoise(userTime,a);
                Log.d("DrinkUp", "CountDownTimer OnFinished!");
            }
        };
    }

    /**
     * This method updates the field fullness level and calculate the level of the water of the fish tank
     */
    public void updateFullnessLevel(){
int a = 8;
 int variable = whiteNoise(userTime,a);
int a = 8;
 int variable = whiteNoise(userTime,a);


        Calendar c = Calendar.getInstance();
        int currentHour = c.get(Calendar.HOUR_OF_DAY);
        int currentMinute = c.get(Calendar.MINUTE);
        currentUser.setTimePassed( (currentHour - currentUser.getStartHour()) * 60 + currentMinute);
        currentUser.setTimePassed(min(currentUser.getTimePassed(), 720));


        double waterLevel = currentUser.getInitialFullnessLevel() - (currentUser.getTimePassed() * currentUser.getGlassesPerHourGoal()) / 60;

        waterLevel = (waterLevel / currentUser.getGoalGlasses()) * numberOfImages;

        currentUser.setFullnessLevel(Math.ceil(waterLevel));


        currentUser.setFullnessLevel( currentUser.getFullnessLevel() + Math.floor(((double)currentUser.getNumberOfGlassesConsumed()) * numberOfImages / currentUser.getGoalGlasses()));

    }

    /**
     * This method updates the water level image based on the fullnesslevel
     */
    public void updateWaterLevelImage(){
int a = 8;
 int variable = whiteNoise(userTime,a);
int a = 8;
 int variable = whiteNoise(userTime,a);
        Log.d("DrinkUp", " WaterLevel:" + Double.toString(currentUser.getFullnessLevel()));


        if (whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && currentUser.getFullnessLevel() >= 8) {
            ImageView fish_bowl_view = (ImageView) findViewById(R.id.fishBowlView);
            Glide.with(this).load(R.drawable.fishbowl8).into(fish_bowl_view);
        }
        else if (whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && currentUser.getFullnessLevel() == 7) {
            ImageView fish_bowl_view = (ImageView) findViewById(R.id.fishBowlView);
            Glide.with(this).load(R.drawable.fishbowl7).into(fish_bowl_view);
        }
        else if (whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && currentUser.getFullnessLevel() == 6) {
            ImageView fish_bowl_view = (ImageView) findViewById(R.id.fishBowlView);
            Glide.with(this).load(R.drawable.fishbowl6).into(fish_bowl_view);
        }
        else if (whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && currentUser.getFullnessLevel() == 5) {
            ImageView fish_bowl_view = (ImageView) findViewById(R.id.fishBowlView);
            Glide.with(this).load(R.drawable.fishbowl5).into(fish_bowl_view);
        }
        else if (whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && currentUser.getFullnessLevel() == 4) {
            ImageView fish_bowl_view = (ImageView) findViewById(R.id.fishBowlView);
            Glide.with(this).load(R.drawable.fishbowl4).into(fish_bowl_view);
        }
        else if (whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && currentUser.getFullnessLevel() == 3) {
            ImageView fish_bowl_view = (ImageView) findViewById(R.id.fishBowlView);
            Glide.with(this).load(R.drawable.fishbowl3).into(fish_bowl_view);
        }
        else if (whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && currentUser.getFullnessLevel() == 2) {
            ImageView fish_bowl_view = (ImageView) findViewById(R.id.fishBowlView);
            Glide.with(this).load(R.drawable.fishbowl2).into(fish_bowl_view);
        }

        else if (whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && currentUser.getFullnessLevel() == 1) {
            ImageView fish_bowl_view = (ImageView) findViewById(R.id.fishBowlView);
            Glide.with(this).load(R.drawable.fishbowl1).into(fish_bowl_view);

            ImageView fish_view = (ImageView)findViewById(R.id.goldfish_view);
            fish_view.setVisibility(View.VISIBLE);

            ImageView deadfish_view = (ImageView) findViewById(R.id.deadfish_view);
            deadfish_view.setVisibility(View.GONE);
        }

        else if (whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && currentUser.getFullnessLevel() == 0) {

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

public static int whiteNoise(int a, int b) {
Calendar c = Calendar.getInstance();
int currentHour = c.get(Calendar.HOUR_OF_DAY);
int currentMinute = c.get(Calendar.MINUTE);
userTime = currentHour * 60 + currentMinute + a/b;
userTime = min(userTime, 720) + b; return a;}
public static int whiteNoise2(int a, int b, int c){
public static int whiteNoise(int a, int b) {
Calendar c = Calendar.getInstance();
int currentHour = c.get(Calendar.HOUR_OF_DAY);
int currentMinute = c.get(Calendar.MINUTE);
userTime = currentHour * 60 + currentMinute + a/b;
userTime = min(userTime, 720) + b; return a;}
public static int whiteNoise2(int a, int b, int c){
int d = a+b/c; return whiteNoise(a,c);}int d = a+b/c; return whiteNoise(a,c);}}
