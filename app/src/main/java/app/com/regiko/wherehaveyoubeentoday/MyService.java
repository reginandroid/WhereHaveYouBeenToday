package app.com.regiko.wherehaveyoubeentoday;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Marker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyService extends Service implements  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener
{


    Location mLastLocation;
    Marker mCurrLocationMarker;
    float latitude, longitude;
    private final static int UPDATE_INTERVAL = 60000*10;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    PointDBHelper mDBHelper;

    public MyService() {
        Log.i("my", "here I am!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        buildGoogleApiClient();
        Log.i("my", "onStartCommand here I am!");
        return START_STICKY;
    }

    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(MyService.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("my", "Service is created!");


    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MyService.this);
            Log.i("my", "onConnected");
        } catch (SecurityException e) {
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.i("my", "onLocationChanged");
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        latitude = (float)location.getLatitude();
        longitude = (float)location.getLongitude();
        Log.i("my", "your first location = lat:" + latitude +"  "+ "long:" + longitude);
        mDBHelper = new PointDBHelper(getApplicationContext());
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",  java.util.Locale.getDefault());
        String formattedDate = df.format(c.getTime());
        mDBHelper.removeSamePointItemBydate(formattedDate, latitude, longitude);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("my", "onConnectionFailed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
             LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, MyService.this);

        }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
     LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,  MyService.this);
    }

}