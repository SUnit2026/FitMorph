package manager;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkoutWorker extends Worker {

    public WorkoutWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params
    ) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {

        SharedPreferences sp =
                getApplicationContext()
                        .getSharedPreferences(
                                "user",
                                Context.MODE_PRIVATE
                        );

        long lastWorkoutTime =
                sp.getLong(
                        "lastWorkoutTime",
                        0
                );

        long now =
                System.currentTimeMillis();

        long diff =
                now - lastWorkoutTime;

        if(diff >= 86400000){

            SharedPreferences.Editor ed =
                    sp.edit();

            ed.putBoolean(
                    "sadMode",
                    true
            );

            ed.apply();
        }

        return Result.success();
    }
}