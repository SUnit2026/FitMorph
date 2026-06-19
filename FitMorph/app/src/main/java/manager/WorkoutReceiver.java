package manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.fitmorph.BackgroundLocationService;

import java.util.concurrent.TimeUnit;

public class WorkoutReceiver
        extends BroadcastReceiver {

    @Override
    public void onReceive(
            Context context,
            Intent intent
    ) {

        if(intent == null)
            return;

        String action =
                intent.getAction();

        if(action == null)
            return;

        if(action.equals(
                Intent.ACTION_BOOT_COMPLETED
        )){

            PeriodicWorkRequest workRequest =
                    new PeriodicWorkRequest.Builder(
                            WorkoutWorker.class,
                            1,
                            TimeUnit.HOURS
                    )
                            .build();

            WorkManager.getInstance(
                    context
            ).enqueueUniquePeriodicWork(
                    "sad_check",
                    ExistingPeriodicWorkPolicy.UPDATE,
                    workRequest
            );

            Intent serviceIntent =
                    new Intent(
                            context,
                            BackgroundLocationService.class
                    );

            try{

                if(android.os.Build.VERSION.SDK_INT >= 26){

                    context.startForegroundService(
                            serviceIntent
                    );

                }else{

                    context.startService(
                            serviceIntent
                    );
                }

            }catch (Exception e){

                e.printStackTrace();
            }
        }
    }
}