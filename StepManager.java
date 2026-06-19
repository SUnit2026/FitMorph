package manager;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class StepManager
        implements SensorEventListener {

    private SharedPreferences sp;

    private SensorManager sensorManager;

    private Sensor stepSensor;

    private int startStep;

    private int todayStep;

    public StepManager(
            SharedPreferences sp,
            SensorManager sensorManager
    ){

        this.sp = sp;

        this.sensorManager =
                sensorManager;

        todayStep =
                sp.getInt(
                        "todayStep",
                        0
                );

        stepSensor =
                sensorManager.getDefaultSensor(
                        Sensor.TYPE_STEP_COUNTER
                );
    }

    public void start(){

        if(stepSensor != null){

            sensorManager.registerListener(
                    this,
                    stepSensor,
                    SensorManager.SENSOR_DELAY_UI
            );
        }
    }

    public void stop(){

        sensorManager.unregisterListener(
                this
        );
    }

    public int getTodayStep(){

        return todayStep;
    }

    public void saveTodayStep(
            int step
    ){

        todayStep = step;

        SharedPreferences.Editor ed =
                sp.edit();

        ed.putInt(
                "todayStep",
                step
        );

        ed.apply();
    }

    public int getStepPoint(){

        return todayStep / 100;
    }

    public int getTotalPoint(){

        return getStepPoint();
    }

    @Override
    public void onSensorChanged(
            SensorEvent event
    ){

        int current =
                (int)event.values[0];

        if(startStep == 0){

            startStep = current;
        }

        todayStep =
                current - startStep;

        SharedPreferences.Editor ed =
                sp.edit();

        ed.putInt(
                "todayStep",
                todayStep
        );

        ed.apply();
    }

    @Override
    public void onAccuracyChanged(
            Sensor sensor,
            int accuracy
    ){

    }
}