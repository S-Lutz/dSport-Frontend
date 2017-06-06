package com.omgproduction.dsport_application.services;

import android.app.LoaderManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.omgproduction.dsport_application.config.LocationKeys;

/**
 * Created by Florian on 22.03.2017.
 */

public class GPS_Service extends Service implements LocationKeys{

    private LocationListener locationListener;
    private LocationManager locationManager;

    private Location prevLocation;
    private float traveledDistance=0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


                if(prevLocation!=null){
                    float distance = location.distanceTo(prevLocation);
                    if(distance>=10)
                        traveledDistance += location.distanceTo(prevLocation);
                }

                Intent i = new Intent(RESULT_INTENT);
                i.putExtra(LOCATION, location);
                i.putExtra(PREV_LOCATION, prevLocation);
                i.putExtra(TRAVELED_DISTANCE, traveledDistance);

                sendBroadcast(i);

                prevLocation = location;

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                //noinspection MissingPermission
                prevLocation = locationManager.getLastKnownLocation(provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,0, locationListener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager!=null){
            locationManager.removeUpdates(locationListener);
        }
    }
}


