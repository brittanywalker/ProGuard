package compsci702.uoa.drinkup;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by Gowri_Rajiv on 23/04/2017.
 */

public class PhoneBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context,Intent intent){
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            //set alarm here
            DrinkUpService.startActionManageDrinkupWaterReminder(context,true);
        }
    }
}