package com.example.fitmorph;

import android.content.Intent;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import manager.WorkoutWorker;

import manager.AutoLoginManager;
import manager.ExerciseManager;
import manager.LocationManagerHelper;
import manager.MoodManager;
import manager.ResetManager;
import manager.StepManager;
import manager.UserManager;

public class HomeActivity extends AppCompatActivity {

    TextView txtInfo;
    TextView txtPoint;
    TextView txtTitle;
    TextView txtWorkout;
    TextView txtDay;
    TextView txtDistance;
    TextView txtStep;
    TextView txtState;

    ImageView imgCharacter;

    Button btnStart;
    Button btnInfo;
    Button btnWorkoutInfo;
    Button btnSetting;

    SharedPreferences sp;

    ExerciseManager exerciseManager;
    UserManager userManager;
    StepManager stepManager;
    LocationManagerHelper locationManagerHelper;
    MoodManager moodManager;
    AutoLoginManager autoLoginManager;

    Handler handler = new Handler();

    Runnable timerRunnable =
            new Runnable() {

                @Override
                public void run() {

                    if(exerciseManager.isWorkout()){

                        exerciseManager.addSecond();

                        updateCharacter();

                        handler.postDelayed(
                                this,
                                1000
                        );
                    }
                }
            };

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(
                savedInstanceState
        );

        setContentView(
                R.layout.activity_home
        );

        txtInfo =
                findViewById(
                        R.id.txtInfo
                );

        txtPoint =
                findViewById(
                        R.id.txtPoint
                );

        txtTitle =
                findViewById(
                        R.id.txtTitle
                );

        txtWorkout =
                findViewById(
                        R.id.txtWorkout
                );

        txtDay =
                findViewById(
                        R.id.txtDay
                );

        txtDistance =
                findViewById(
                        R.id.txtDistance
                );

        txtStep =
                findViewById(
                        R.id.txtStep
                );

        txtState =
                findViewById(
                        R.id.txtState
                );

        imgCharacter =
                findViewById(
                        R.id.imgCharacter
                );

        btnStart =
                findViewById(
                        R.id.btnStart
                );

        btnInfo =
                findViewById(
                        R.id.btnInfo
                );

        btnWorkoutInfo =
                findViewById(
                        R.id.btnWorkoutInfo
                );

        btnSetting =
                findViewById(
                        R.id.btnSetting
                );

        sp =
                getSharedPreferences(
                        "user",
                        MODE_PRIVATE
                );

        PeriodicWorkRequest workRequest =
                new PeriodicWorkRequest.Builder(
                        WorkoutWorker.class,
                        1,
                        TimeUnit.HOURS
                )
                        .build();

        WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork(
                        "sad_check",
                        ExistingPeriodicWorkPolicy.UPDATE,
                        workRequest
                );

        Intent serviceIntent =
                new Intent(
                        this,
                        BackgroundLocationService.class
                );

        if(android.os.Build.VERSION.SDK_INT >= 26){

            startForegroundService(
                    serviceIntent
            );

        }else{

            startService(
                    serviceIntent
            );
        }

        ResetManager.checkReset(sp);

        exerciseManager =
                new ExerciseManager(sp);

        userManager =
                new UserManager(sp);

        moodManager =
                new MoodManager(sp);

        autoLoginManager =
                new AutoLoginManager(sp);

        SensorManager sensorManager =
                (SensorManager)
                        getSystemService(
                                SENSOR_SERVICE
                        );

        stepManager =
                new StepManager(
                        sp,
                        sensorManager
                );

        LocationManager locationManager =
                (LocationManager)
                        getSystemService(
                                LOCATION_SERVICE
                        );

        locationManagerHelper =
                new LocationManagerHelper(
                        sp,
                        locationManager
                );

        stepManager.start();

        locationManagerHelper.start();

        exerciseManager.checkSadMode();

        updateCharacter();
        btnStart.setOnClickListener(v -> {

            if(!exerciseManager.isWorkout()){

                exerciseManager.startWorkout();

                moodManager.updateWorkoutTime();

                btnStart.setText(
                        "운동 종료"
                );

                handler.post(
                        timerRunnable
                );
            }
            else{

                exerciseManager.stopWorkout();

                btnStart.setText(
                        "운동 시작"
                );
            }

            updateCharacter();
        });

        btnInfo.setOnClickListener(v -> {

            View view =
                    LayoutInflater.from(this)
                            .inflate(
                                    R.layout.dialog_user_edit,
                                    null
                            );

            EditText edtAge =
                    view.findViewById(
                            R.id.edtEditAge
                    );

            EditText edtHeight =
                    view.findViewById(
                            R.id.edtEditHeight
                    );

            EditText edtWeight =
                    view.findViewById(
                            R.id.edtEditWeight
                    );

            TextView txtUserInfo =
                    view.findViewById(
                            R.id.txtUserInfo
                    );

            txtUserInfo.setText(
                    userManager.getUserInfo()
            );

            edtAge.setText(
                    userManager.getAge()
            );

            edtHeight.setText(
                    userManager.getHeight()
            );

            edtWeight.setText(
                    userManager.getWeight()
            );

            new AlertDialog.Builder(this)
                    .setTitle("내 정보")
                    .setView(view)

                    .setPositiveButton(
                            "저장",
                            (d,w)->{

                                userManager.updateAge(
                                        edtAge.getText()
                                                .toString()
                                );

                                userManager.updateHeight(
                                        edtHeight.getText()
                                                .toString()
                                );

                                userManager.updateWeight(
                                        edtWeight.getText()
                                                .toString()
                                );

                                updateCharacter();
                            }
                    )

                    .setNegativeButton(
                            "닫기",
                            null
                    )

                    .show();
        });

        btnWorkoutInfo.setOnClickListener(v -> {

            String msg =
                    "총 운동시간 : "
                            + exerciseManager.getWorkoutMinute()
                            + "분\n\n"

                            + "총 운동일수 : "
                            + exerciseManager.getWorkoutDay()
                            + "일\n\n"

                            + "걸음수 : "
                            + stepManager.getTodayStep()
                            + "걸음\n\n"

                            + "이동거리 : "
                            + String.format(
                            "%.2f",
                            locationManagerHelper.getDistance()
                    )
                            + " km";

            new AlertDialog.Builder(this)
                    .setTitle(
                            "운동 정보"
                    )
                    .setMessage(
                            msg
                    )
                    .setPositiveButton(
                            "확인",
                            null
                    )
                    .show();
        });

        btnSetting.setOnClickListener(v -> {

            View view =
                    LayoutInflater.from(this)
                            .inflate(
                                    R.layout.dialog_gym,
                                    null
                            );

            EditText edtGymName =
                    view.findViewById(
                            R.id.edtGymName
                    );

            Button btnSaveCurrentLocation =
                    view.findViewById(
                            R.id.btnSaveCurrentLocation
                    );

            TextView txtCurrentLocation =
                    view.findViewById(
                            R.id.txtCurrentLocation
                    );

            btnSaveCurrentLocation
                    .setOnClickListener(v2 -> {

                        if(locationManagerHelper
                                .hasCurrentLocation()){

                            txtCurrentLocation.setText(
                                    "현재 위치 저장 준비 완료\n\n위도 : "
                                            + locationManagerHelper.getCurrentLat()
                                            + "\n경도 : "
                                            + locationManagerHelper.getCurrentLng()
                            );

                        }else{

                            Toast.makeText(
                                    this,
                                    "GPS 위치를 찾는 중입니다.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });

            new AlertDialog.Builder(this)
                    .setTitle(
                            "운동장 등록"
                    )
                    .setView(view)

                    .setPositiveButton(
                            "저장",
                            (d,w)->{

                                locationManagerHelper
                                        .saveGymLocation(
                                                edtGymName
                                                        .getText()
                                                        .toString()
                                        );

                                Toast.makeText(
                                        this,
                                        "운동장 저장 완료",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                    )

                    .setNegativeButton(
                            "취소",
                            null
                    )

                    .show();
        });
    }
    private void updateCharacter(){

        int point =
                exerciseManager.getPoint()
                        + stepManager.getTotalPoint()
                        + locationManagerHelper.getDistancePoint();

        txtPoint.setText(
                "포인트 : " + point
        );

        txtWorkout.setText(
                "오늘 운동시간 : "
                        + exerciseManager.getWorkoutMinute()
                        + "분"
        );

        txtStep.setText(
                "오늘 걸음수 : "
                        + stepManager.getTodayStep()
        );

        txtDistance.setText(
                "오늘 이동거리 : "
                        + String.format(
                        "%.2f",
                        locationManagerHelper.getDistance()
                )
                        + " km"
        );

        txtTitle.setText(
                "칭호 : "
                        + exerciseManager.getTitle()
        );

        txtState.setText(
                "상태 : "
                        + exerciseManager.getState()
        );

        txtDay.setText(
                String.valueOf(
                        exerciseManager.getWorkoutDay()
                )
        );

        int stage =
                exerciseManager
                        .getCharacterStage();

        if(moodManager.isSadMode()){

            imgCharacter.setImageResource(
                    R.drawable.sad
            );
        }
        else if(stage == 1){

            imgCharacter.setImageResource(
                    R.drawable.fat
            );
        }
        else if(stage == 2){

            imgCharacter.setImageResource(
                    R.drawable.normal
            );
        }
        else{

            imgCharacter.setImageResource(
                    R.drawable.muscle
            );
        }

        if(locationManagerHelper.hasGymLocation()){

            float distance =
                    locationManagerHelper
                            .getDistanceToGym();

            txtInfo.setText(
                    "등록 운동장 : "
                            + locationManagerHelper
                            .getGymName()

                            + "\n운동장 거리 : "
                            + String.format(
                            "%.0f",
                            distance
                    )
                            + "m"
            );
        }
        else{

            txtInfo.setText(
                    "등록된 운동장이 없습니다."
            );
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        stepManager.stop();

        locationManagerHelper.stop();

        handler.removeCallbacks(
                timerRunnable
        );
    }
}