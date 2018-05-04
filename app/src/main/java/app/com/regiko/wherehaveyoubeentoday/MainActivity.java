package app.com.regiko.wherehaveyoubeentoday;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CalendarView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity  {
    Intent mServiceIntent, mBroadcastIntent;
    private MyService mMyService;
    Context ctx;
    LocationManager locationManager;
    private CalendarView mCalendarView;

   public Context getCtx() {
        return ctx;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        checkLocationPermission();
        mMyService = new MyService();
        mServiceIntent = new Intent(getCtx(),MyService.class);
        mBroadcastIntent = new Intent(getCtx(), MyReceiver.class);
        if (!isMyServiceRunning(mMyService.getClass())) {
        PendingIntent recurringAlarm = PendingIntent.getService(getApplicationContext(), 0, mServiceIntent, 0);
        AlarmManager alarms = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Calendar updateTime = Calendar.getInstance();
        updateTime.setTimeInMillis(System.currentTimeMillis());

        alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP, updateTime.getTimeInMillis(), 60000*10, recurringAlarm);
        }
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView CalendarView, int year, int month, int dayOfMonth) {
                month = month+1;
                String correctedmonth=null, correctedday=null;
                if (month <10){
                    correctedmonth = "0" + month;
                }
                else correctedmonth = String.valueOf(month);
                if (dayOfMonth <10){
                    correctedday = "0" + dayOfMonth;
                }
                else correctedday =String.valueOf(dayOfMonth);
                String date = year + "-" + correctedmonth + "-"+ correctedday ;
                Log.d("my", "onSelectedDayChange: yyyy-mm-dd:" + date);
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("date",date);
                startActivity(intent);

            }
        });


    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.d ("my", true+"");
                return true;
            }
        }
        Log.i("my", false+"");
        return false;
    }

    @Override
    protected void onDestroy() {
       stopService(mServiceIntent);
        Log.i("my", "onDestroy!");
        super.onDestroy();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                                              ActivityCompat.requestPermissions((Activity) getCtx(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

}
