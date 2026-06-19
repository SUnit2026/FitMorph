package manager;

import android.content.SharedPreferences;

public class MoodManager {

    private SharedPreferences sp;

    public MoodManager(
            SharedPreferences sp
    ){
        this.sp = sp;
    }

    public boolean isSadMode(){

        return sp.getBoolean(
                "sadMode",
                false
        );
    }

    public void setSadMode(
            boolean sad
    ){

        SharedPreferences.Editor ed =
                sp.edit();

        ed.putBoolean(
                "sadMode",
                sad);

        ed.apply();
    }

    public String getStateText(
            boolean isWorkout,
            int point
    ){

        if(isWorkout){

            return "기쁜 상태";
        }

        if(isSadMode()){

            return "슬픈 상태";
        }

        if(point >= 30){

            return "건강한 상태";
        }

        return "보통 상태";
    }

    public int getCharacterImage(
            int point
    ){

        if(isSadMode()){

            return 1;
        }

        if(point < 300){

            return 1;
        }

        if(point < 800){

            return 2;
        }

        return 3;
    }

    public void updateWorkoutTime(){

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
}