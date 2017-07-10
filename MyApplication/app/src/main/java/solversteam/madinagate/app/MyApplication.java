package solversteam.madinagate.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import solversteam.madinagate.helper.LocaleHelper;

/**
 * Created by root on 4/26/17.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

    }

    public static Context getContext(){
        return context;
    }
}
