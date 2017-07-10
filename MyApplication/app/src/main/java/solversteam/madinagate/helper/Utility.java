package solversteam.madinagate.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import solversteam.madinagate.R;
import solversteam.madinagate.app.MyApplication;


/**
 * Created by root on 4/11/17.
 */

public class Utility {
    public static void resizeFont(TextView tv){
        //set font size
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());

        if (prefs.getString("font_size","").equals(""))return ;
        int font_sz = Integer.parseInt(prefs.getString("font_size",""));
        Log.e("FOUNT_SIZE : " , prefs + "");
        tv.setTextSize(tv.getTextSize() - 10 + (font_sz * 2));
    }

    public static void nightMode_on (){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    public static void nightMode_off (){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    public static void doNightMode(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        if (!prefs.getBoolean("night_mode",false))
            nightMode_off();
        else
            nightMode_on();


    }

    public static void doNightMode(boolean on){
        if (!on)
            nightMode_off();
        else
            nightMode_on();


    }
    public static void connectionToast(Context _context) {

        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.my_customtoast, null);
        LinearLayout linear = (LinearLayout) layout.findViewById(R.id.Forget_customlayout_LinearLayout);
        Toast toast = new Toast(_context);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public static Drawable drawableFromUrl(String url) throws IOException {
        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(x);
    }

    public static ArrayList<Integer> generateDistinctRandomNumbers(int size, int from, int to){
        if (from > to){
            int temp = from;
            from = to;
            to = temp;
        }
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i=from; i<to; i++) {
            list.add(new Integer(i));
        }
        Collections.shuffle(list);
        for (int i=0; i<size; i++) {
            System.out.println(list.get(i));
        }

        return list;
    }


}
