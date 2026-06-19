package manager;

import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExerciseManager {

    private SharedPreferences sp;

    private boolean isWorkout = false;

    private long totalWorkoutSec;

    public ExerciseManager(
            SharedPreferences sp
    ){
        this.sp = sp;

        totalWorkoutSec =
                sp.getLong(
                        "workoutSec",
                        0
                );
    }

    public boolean isWorkout(){

        return isWorkout;
    }

    public void startWorkout(){

        isWorkout = true;

        SharedPreferences.Editor ed =
                sp.edit();

        ed.putLong(
                "lastWorkoutTime",
                System.currentTimeMillis()
        );

        ed.putBoolean(
                "sadMode",
                false
        );

        ed.apply();
    }

    public void stopWorkout(){

        isWorkout = false;

        addWorkoutDay();

        SharedPreferences.Editor ed =
                sp.edit();

        ed.putLong(
                "lastWorkoutTime",
                System.currentTimeMillis()
        );

        ed.apply();
    }

    public void addSecond(){

        if(!isWorkout)
            return;

        totalWorkoutSec++;

        save();
    }

    public long getWorkoutSec(){

        return totalWorkoutSec;
    }

    public int getWorkoutMinute(){

        return (int)(totalWorkoutSec / 60);
    }

    public int getPoint(){

        return getWorkoutMinute();
    }

    public int getWorkoutDay(){

        return sp.getInt(
                "workoutDay",
                0
        );
    }

    private void save(){

        SharedPreferences.Editor ed =
                sp.edit();

        ed.putLong(
                "workoutSec",
                totalWorkoutSec
        );

        ed.putInt(
                "point",
                getPoint()
        );

        ed.apply();
    }

    private void addWorkoutDay(){

        String today =
                new SimpleDateFormat(
                        "yyyyMMdd",
                        Locale.getDefault()
                ).format(
                        new Date()
                );

        String lastDate =
                sp.getString(
                        "lastWorkoutDate",
                        ""
                );

        if(!today.equals(lastDate)){

            SharedPreferences.Editor ed =
                    sp.edit();

            ed.putInt(
                    "workoutDay",
                    getWorkoutDay() + 1
            );

            ed.putString(
                    "lastWorkoutDate",
                    today
            );

            ed.apply();
        }
    }

    public String getTitle(){

        int day =
                getWorkoutDay();

        if(day < 3)
            return "운동 새싹";

        if(day < 7)
            return "초보 운동러";

        if(day < 30)
            return "꾸준한 운동러";

        if(day < 100)
            return "운동 중독자";

        return "전설의 운동러";
    }

    public String getState(){

        boolean sad =
                sp.getBoolean(
                        "sadMode",
                        false
                );

        if(isWorkout)
            return "기쁜 상태";

        if(sad)
            return "슬픈 상태";

        return "건강한 상태";
    }

    public int getCharacterStage(){

        int point =
                getPoint();

        if(point < 300)
            return 1;

        if(point < 800)
            return 2;

        return 3;
    }

    public void checkSadMode(){

        long lastWorkout =
                sp.getLong(
                        "lastWorkoutTime",
                        0
                );

        long now =
                System.currentTimeMillis();

        long diff =
                now - lastWorkout;

        if(diff > 86400000){

            SharedPreferences.Editor ed =
                    sp.edit();

            ed.putBoolean(
                    "sadMode",
                    true
            );

            ed.apply();
        }
    }
}