package solversteam.madinagate.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import solversteam.madinagate.R;
import solversteam.madinagate.helper.LocaleHelper;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    private TextView lanTextView;
    private String language_code;
    private int index;

    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(solversteam.madinagate.R.layout.activity_setting);

        SharedPreferences prefs = getSharedPreferences(getString(solversteam.madinagate.R.string.languages_file_key), MODE_PRIVATE);
        index = prefs.getInt(getString(solversteam.madinagate.R.string.languages_key), 1);
        language_code = getResources().getStringArray(R.array.languages_code)[index];
        updateViews(language_code);
    }

    private void updateViews(String languageCode) {
        Log.e("SettingActivity : ", "Lang code = " + languageCode);
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        init();

    }

    private void init(){
        setTitle(getString(solversteam.madinagate.R.string.settings));
        lanTextView = (TextView) findViewById(solversteam.madinagate.R.id.language_tv);
        lanTextView.setText(getString(solversteam.madinagate.R.string.language));
        lanTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this, solversteam.madinagate.R.style.MyDialogTheme);
                builder.setTitle(getApplicationContext().getString(solversteam.madinagate.R.string.language));
                builder.setSingleChoiceItems(solversteam.madinagate.R.array.languages, index,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final String[] languages = SettingActivity.this.getResources().getStringArray(solversteam.madinagate.R.array.languages_code);

                                Log.e("SettingActivity", "lang = " + languages[i]);
                                SharedPreferences prefs = getSharedPreferences(getString(solversteam.madinagate.R.string.languages_file_key), MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putInt(getString(solversteam.madinagate.R.string.languages_key), i);
                                editor.commit();

                                Intent intent = new Intent(SettingActivity.this, Splash.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                ActivityCompat.finishAffinity(SettingActivity.this);
                                startActivity(intent);
                                SettingActivity.this.finish();
                            }
                        }
                );

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    public void finish() {
        super.finish();
        if(alert != null){
            alert = null;
        }
    }

}
