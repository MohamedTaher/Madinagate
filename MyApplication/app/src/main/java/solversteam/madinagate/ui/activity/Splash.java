package solversteam.madinagate.ui.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import solversteam.madinagate.R;
import solversteam.madinagate.data.connection.Connection;
import solversteam.madinagate.data.connection.ConnectionDetector;
import solversteam.madinagate.helper.Utility;


/**
 * Created by taher on 4/25/17.
 */

public class Splash extends AppCompatActivity {

    private Connection connection;
    private ConnectionDetector connectionDetector;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(solversteam.madinagate.R.layout.activity_splash);
        getSupportActionBar().hide();
        imageView = (ImageView)findViewById(R.id.splash_img);

        Log.e("check lan", Locale.getDefault().getLanguage());

        init();

    }


    private void init() {
        connectionDetector=new ConnectionDetector(this);

        if(connectionDetector.isConnectingToInternet()) {
            connection = new Connection(this, "/getallappresources/" + 1, "Get");
            connection.reset();
            connection.Connect(new Connection.Result() {
                @Override
                public void data(String str) throws JSONException {
                    Log.e("checkresponseresult",str);

                    JSONObject jsonObject = new JSONObject(str);
                    JSONArray jsonArray = jsonObject.getJSONArray("1-appresources");
                    jsonObject = (JSONObject)jsonArray.get(1);
                    String url = (String) jsonObject.get("resourceurl");
                    Log.e("splash url", url);


                    Picasso.with(Splash.this).load(url).fit().into(imageView);



                    int secondsDelayed = 2;
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            startActivity(new Intent(Splash.this, HomeActivity.class));
                            finish();
                        }
                    }, secondsDelayed * 1000);

                }
            });

        }
        else {
            Utility.connectionToast(this);
        }

    }
}
