package manager;

import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ResetManager {

    public static void checkReset(
            SharedPreferences sp
    ){

        String today =
                new SimpleDateFormat(
                        "yyyyMMdd",
                        Locale.getDefault()
                ).format(
                        new Date()
                );

        String saveDate =
                sp.getString(
                        "resetDate",
                        ""
                );

        if(!today.equals(saveDate)){

            SharedPreferences.Editor ed =
                    sp.edit();

            ed.putString(
                    "resetDate",
                    today
            );

            ed.putInt(
                    "todayStep",
                    0
            );

            ed.putFloat(
                    "todayDistance",
                    0f
            );

            ed.putBoolean(
                    "sadMode",
                    false
            );

            ed.apply();
        }
    }

    public static boolean isNewDay(
            SharedPreferences sp
    ){

        String today =
                new SimpleDateFormat(
                        "yyyyMMdd",
                        Locale.getDefault()
                ).format(
                        new Date()
                );

        String saveDate =
                sp.getString(
                        "resetDate",
                        ""
                );

        return !today.equals(saveDate);
    }

    public static void forceReset(
            SharedPreferences sp
    ){

        SharedPreferences.Editor ed =
                sp.edit();

        ed.putInt(
                "todayStep",
                0
        );

        ed.putFloat(
                "todayDistance",
                0f
        );

        ed.putString(
                "resetDate",
                new SimpleDateFormat(
                        "yyyyMMdd",
                        Locale.getDefault()
                ).format(
                        new Date()
                )
        );

        ed.apply();
    }
}