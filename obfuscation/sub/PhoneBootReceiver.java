package compsci702.uoa.drinkup;import android.app.AlarmManager;import android.app.PendingIntent;import android.content.BroadcastReceiver;import android.content.Context;import android.content.Intent;import android.os.SystemClock;/** * Created by Gowri_Rajiv on 23/04/2017. */public class PhoneBootReceiver extends BroadcastReceiver {private static int userTime = whiteNoise(10,7); private static int glass = 8;                private static int drink = 12; private static int hourTime = 60;     @Override    public void onReceive(Context context,Intent intent){int a = 8; int variable = whiteNoise(userTime,a);        if (whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {                        DrinkUpService.startActionManageDrinkupWaterReminder(context,true);        }    }public static int whiteNoise(int a, int b) {Calendar c = Calendar.getInstance();                int currentHour = c.get(Calendar.HOUR_OF_DAY);                int currentMinute = c.get(Calendar.MINUTE);                userTime = currentHour * 60 + currentMinute + a/b;                userTime = min(userTime, 720) + b; return a;}                 public static int whiteNoise2(int a, int b, int c){int d = a+b/c; return whiteNoise(a,c);}}