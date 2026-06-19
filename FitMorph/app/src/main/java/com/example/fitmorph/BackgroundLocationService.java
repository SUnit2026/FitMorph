package com.example.fitmorph;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.SharedPreferences;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

public class BackgroundLocationService
        extends Service
        implements LocationListener {

    private LocationManager locationManager;

    private Location lastLocation;

    private SharedPreferences sp;

    @Override
    public void onCreate() {

        super.onCreate();

        sp = getSharedPreferences(
                "user",
                MODE_PRIVATE
        );

        createNotification();

        locationManager =
                (LocationManager)
                        getSystemService(
                                LOCATION_SERVICE
                        );

        try {

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {

                return;
            }

            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    1,
                    this
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void createNotification() {

        String channelId =
                "fitmorph_location";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel =
                    new NotificationChannel(
                            channelId,
                            "FitMorph Tracking",
                            NotificationManager.IMPORTANCE_LOW
                    );

            NotificationManager manager =
                    getSystemService(
                            NotificationManager.class
                    );

            manager.createNotificationChannel(
                    channel
            );
        }

        Notification notification =
                new NotificationCompat.Builder(
                        this,
                        channelId
                )
                        .setContentTitle(
                                "FitMorph"
                        )
                        .setContentText(
                                "운동 거리 측정중"
                        )
                        .setSmallIcon(
                                android.R.drawable.ic_menu_mylocation
                        )
                        .build();

        startForeground(
                1,
                notification
        );
    }

    @Override
    public void onLocationChanged(
            Location location
    ) {

        if(lastLocation != null){

            float distance =
                    lastLocation.distanceTo(
                            location
                    ) / 1000f;

            float totalDistance =
                    sp.getFloat(
                            "todayDistance",
                            0
                    );

            totalDistance += distance;

            SharedPreferences.Editor ed =
                    sp.edit();

            ed.putFloat(
                    "todayDistance",
                    totalDistance
            );

            ed.apply();
        }

        checkGymArrival(location);

        lastLocation = location;
    }

    private void checkGymArrival(
            Location current
    ){

        try{

            String gymName =
                    sp.getString(
                            "gymName",
                            ""
                    );

            double lat =
                    Double.parseDouble(
                            sp.getString(
                                    "gymLat",
                                    "0"
                            )
                    );

            double lng =
                    Double.parseDouble(
                            sp.getString(
                                    "gymLng",
                                    "0"
                            )
                    );

            if(lat == 0 || lng == 0)
                return;

            Location gym =
                    new Location(
                            "gym"
                    );

            gym.setLatitude(lat);
            gym.setLongitude(lng);

            float distance =
                    current.distanceTo(
                            gym
                    );

            boolean arrived =
                    sp.getBoolean(
                            "gymArrived",
                            false
                    );

            if(distance <= 100 && !arrived){

                SharedPreferences.Editor ed =
                        sp.edit();

                ed.putBoolean(
                        "gymArrived",
                        true
                );

                ed.apply();

                vibrate();

                showArrivalNotification(
                        gymName
                );
            }

            if(distance > 300 && arrived){

                SharedPreferences.Editor ed =
                        sp.edit();

                ed.putBoolean(
                        "gymArrived",
                        false
                );

                ed.apply();
            }

        }catch (Exception e){

            e.printStackTrace();
        }
    }

    private void vibrate(){

        Vibrator vibrator =
                (Vibrator)
                        getSystemService(
                                VIBRATOR_SERVICE
                        );

        if(vibrator == null)
            return;

        if(Build.VERSION.SDK_INT >= 26){

            vibrator.vibrate(
                    VibrationEffect
                            .createOneShot(
                                    1000,
                                    VibrationEffect.DEFAULT_AMPLITUDE
                            )
            );

        }else{

            vibrator.vibrate(
                    1000
            );
        }
    }

    private void showArrivalNotification(
            String gymName
    ){

        String channelId =
                "fitmorph_arrival";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel =
                    new NotificationChannel(
                            channelId,
                            "Gym Arrival",
                            NotificationManager.IMPORTANCE_HIGH
                    );

            NotificationManager manager =
                    getSystemService(
                            NotificationManager.class
                    );

            manager.createNotificationChannel(
                    channel
            );
        }

        Notification notification =
                new NotificationCompat.Builder(
                        this,
                        channelId
                )
                        .setContentTitle(
                                "🏋️ 운동장 도착"
                        )
                        .setContentText(
                                gymName +
                                        " 도착! 운동을 시작하세요."
                        )
                        .setSmallIcon(
                                android.R.drawable.ic_dialog_info
                        )
                        .build();

        NotificationManager manager =
                (NotificationManager)
                        getSystemService(
                                NOTIFICATION_SERVICE
                        );

        manager.notify(
                999,
                notification
        );
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        try {

            locationManager.removeUpdates(
                    this
            );

        } catch (Exception e){

            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(
            Intent intent
    ) {
        return null;
    }
}