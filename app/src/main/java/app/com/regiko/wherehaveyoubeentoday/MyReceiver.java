package app.com.regiko.wherehaveyoubeentoday;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("my", "Service Stops! Oooooooooooooppppssssss!!!!");
        context.startService(new Intent(context, MyService.class));;

    }
}
