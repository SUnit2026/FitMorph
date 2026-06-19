package manager;

import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationManagerHelper
        implements LocationListener {

    private SharedPreferences sp;

    private LocationManager locationManager;

    private Location lastLocation;

    private Location currentLocation;

    private float totalDistance;

    public LocationManagerHelper(
            SharedPreferences sp,
            LocationManager locationManager
    ){

        this.sp = sp;

        this.locationManager =
                locationManager;

        totalDistance =
                sp.getFloat(
                        "todayDistance",
                        0
                );
    }

    public void start(){

        try{

            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    3000,
                    1,
                    this
            );

        }catch (SecurityException e){

            e.printStackTrace();
        }
    }

    public void stop(){

        try{

            locationManager.removeUpdates(
                    this
            );

        }catch (SecurityException e){

            e.printStackTrace();
        }
    }

    public float getDistance(){

        return totalDistance;
    }

    public void saveDistance(
            float distance
    ){

        totalDistance = distance;

        SharedPreferences.Editor ed =
                sp.edit();

        ed.putFloat(
                "todayDistance",
                distance
        );

        ed.apply();
    }

    public int getDistancePoint(){

        return (int)(totalDistance * 5);
    }

    @Override
    public void onLocationChanged(
            Location location
    ){

        currentLocation = location;

        if(lastLocation != null){

            float distance =
                    lastLocation.distanceTo(
                            location
                    ) / 1000f;

            totalDistance += distance;

            SharedPreferences.Editor ed =
                    sp.edit();

            ed.putFloat(
                    "todayDistance",
                    totalDistance
            );

            ed.apply();
        }

        lastLocation = location;
    }

    @Override
    public void onStatusChanged(
            String provider,
            int status,
            Bundle extras
    ){

    }

    @Override
    public void onProviderEnabled(
            String provider
    ){

    }

    @Override
    public void onProviderDisabled(
            String provider
    ){

    }

    public boolean hasCurrentLocation(){

        return currentLocation != null;
    }

    public double getCurrentLat(){

        if(currentLocation == null)
            return 0;

        return currentLocation.getLatitude();
    }

    public double getCurrentLng(){

        if(currentLocation == null)
            return 0;

        return currentLocation.getLongitude();
    }

    public void saveGymLocation(
            String name
    ){

        if(currentLocation == null)
            return;

        SharedPreferences.Editor ed =
                sp.edit();

        ed.putString(
                "gymName",
                name
        );

        ed.putString(
                "gymLat",
                String.valueOf(
                        currentLocation.getLatitude()
                )
        );

        ed.putString(
                "gymLng",
                String.valueOf(
                        currentLocation.getLongitude()
                )
        );

        ed.apply();
    }

    public String getGymName(){

        return sp.getString(
                "gymName",
                "미등록"
        );
    }

    public double getGymLat(){

        try{

            return Double.parseDouble(
                    sp.getString(
                            "gymLat",
                            "0"
                    )
            );

        }catch (Exception e){

            return 0;
        }
    }

    public double getGymLng(){

        try{

            return Double.parseDouble(
                    sp.getString(
                            "gymLng",
                            "0"
                    )
            );

        }catch (Exception e){

            return 0;
        }
    }

    public boolean hasGymLocation(){

        return getGymLat() != 0
                && getGymLng() != 0;
    }

    public float getDistanceToGym(){

        if(currentLocation == null)
            return -1;

        if(!hasGymLocation())
            return -1;

        Location gym =
                new Location(
                        "gym"
                );

        gym.setLatitude(
                getGymLat()
        );

        gym.setLongitude(
                getGymLng()
        );

        return currentLocation.distanceTo(
                gym
        );
    }
}