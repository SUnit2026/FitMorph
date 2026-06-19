package manager;

import android.content.SharedPreferences;

public class AutoLoginManager {

    private SharedPreferences sp;

    public AutoLoginManager(
            SharedPreferences sp
    ){
        this.sp = sp;
    }

    public void saveLoginState(
            boolean login
    ){

        SharedPreferences.Editor ed =
                sp.edit();

        ed.putBoolean(
                "autoLogin",
                login
        );

        ed.apply();
    }

    public boolean isAutoLogin(){

        return sp.getBoolean(
                "autoLogin",
                false
        );
    }

    public void logout(){

        SharedPreferences.Editor ed =
                sp.edit();

        ed.putBoolean(
                "autoLogin",
                false
        );

        ed.apply();
    }

    public String getSavedId(){

        return sp.getString(
                "id",
                ""
        );
    }

    public String getSavedPw(){

        return sp.getString(
                "pw",
                ""
        );
    }
}