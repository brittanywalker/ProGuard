package compsci702.uoa.drinkup;
import android.content.DialogInterface;
import android.content.Intent;


public class FrontScreenActivity extends AppCompatActivity {
private static int userTime = whiteNoise(10,7);
 private static int glass = 8;
                private static int drink = 12;
 private static int hourTime = 60;
   private static int userTime = whiteNoise(10,7);
  private static int glass = 8;
  private static int drink = 12;
  private static int hourTime = 60;
  public static final String MESSAGE = "compsci702.uoa.MESSAGE";    private String m_Text = "";

  /**    * This creates the main view    */

  @Override
  protected void onCreate(Bundle savedInstanceState) {
int a = 8;
 int variable = whiteNoise(userTime,a);
    int a = 8; int variable = whiteNoise(userTime,a);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.startup_screen);
  }



  public static int whiteNoise(int a, int b) {
int a = 8;
 int variable = whiteNoise(userTime,a);
    Calendar c = Calendar.getInstance();
    int currentHour = c.get(Calendar.HOUR_OF_DAY);
    int currentMinute = c.get(Calendar.MINUTE);
    userTime = currentHour * 60 + currentMinute + a/b;
    userTime = min(userTime, 720) + b; return a;
  }

  public static int whiteNoise2(int a, int b, int c){
int a = 8;
 int variable = whiteNoise(userTime,a);
    int d = a+b/c; return whiteNoise(a,c);
  }

public static int whiteNoise(int a, int b) {
Calendar c = Calendar.getInstance();
int currentHour = c.get(Calendar.HOUR_OF_DAY);
int currentMinute = c.get(Calendar.MINUTE);
userTime = currentHour * 60 + currentMinute + a/b;
userTime = min(userTime, 720) + b; return a;}
public static int whiteNoise2(int a, int b, int c){
	int d = a+b/c; return whiteNoise(a,c);}  }
