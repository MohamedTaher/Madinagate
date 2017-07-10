package solversteam.madinagate.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import solversteam.madinagate.R;
import solversteam.madinagate.data.Constants;
import solversteam.madinagate.data.connection.Connection;
import solversteam.madinagate.data.connection.ConnectionDetector;
import solversteam.madinagate.helper.LocaleHelper;
import solversteam.madinagate.helper.Utility;
import solversteam.madinagate.model.Section;
import solversteam.madinagate.ui.adabter.ExpandableListAdapter;
import solversteam.madinagate.ui.adabter.HomeListAdapter;

public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView about, contact;
    protected ExpandableListAdapter menuListAdapter;
    protected ExpandableListView menuExpListView;

    protected List<String> menuListDataHeader;
    protected HashMap<String, List<String>> menuListDataChild;

    protected String language_code;

    protected DrawerLayout drawer;

    protected Connection connection;
    protected ConnectionDetector connectionDetector;
    private Dialog dialog;


    private ArrayList<Section> data = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        NavigationView navigationView = (NavigationView) findViewById(solversteam.madinagate.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences prefs = getSharedPreferences(getString(solversteam.madinagate.R.string.languages_file_key), MODE_PRIVATE);
        int index = prefs.getInt(getString(solversteam.madinagate.R.string.languages_key), 1);
        language_code = getResources().getStringArray(R.array.languages_code)[index];
        updateViews(language_code);
    }

    private void initToolbar() {
        //custom toolbar

        drawer = (DrawerLayout) findViewById(solversteam.madinagate.R.id.drawer_layout);
        Log.e("MainMenu", "Srart");
        ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                solversteam.madinagate.R.layout.toolbar,
                null);

        // Set up your ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);

        Toolbar toolbar=(Toolbar)actionBar.getCustomView().getParent();
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.getContentInsetEnd();
        toolbar.setPadding(0, 0, 0, 0);

        ImageView menuIcon = (ImageView) findViewById(solversteam.madinagate.R.id.menu_icon);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo "if English language the gravity mast be LEFT"
                if (language_code.equals("en-us")) {
                    if (drawer.isDrawerOpen(Gravity.LEFT))
                        drawer.closeDrawer(Gravity.LEFT);
                    else
                        drawer.openDrawer(Gravity.LEFT);

                } else if (language_code.equals("ar")) {
                    if (drawer.isDrawerOpen(Gravity.RIGHT))
                        drawer.closeDrawer(Gravity.RIGHT);
                    else
                        drawer.openDrawer(Gravity.RIGHT);
                } else {
                    if (drawer.isDrawerOpen(Gravity.LEFT))
                        drawer.closeDrawer(Gravity.LEFT);
                    else
                        drawer.openDrawer(Gravity.LEFT);
                }

            }
        });

        ImageView searchIcon = (ImageView) findViewById(solversteam.madinagate.R.id.searchIcon);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo search
                startActivity(new Intent(MainMenuActivity.this, SearchActivity.class));
            }
        });

        ImageView settingIcon = (ImageView) findViewById(solversteam.madinagate.R.id.settingIcon);
        settingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                overridePendingTransition(solversteam.madinagate.R.anim.slide_in_top, solversteam.madinagate.R.anim.no_thing);
            }
        });

        ImageView logo = (ImageView) findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainMenuActivity.this.startActivity(new Intent(MainMenuActivity.this,HomeActivity.class));
            }
        });
    }

    private void initMenu(){
        // get the listview
        menuExpListView = (ExpandableListView) findViewById(solversteam.madinagate.R.id.menu_expand);

        about = (TextView) findViewById(R.id.about);
        contact = (TextView) findViewById(R.id.connect);

        SharedPreferences prefs = getSharedPreferences(getString(solversteam.madinagate.R.string.languages_file_key), MODE_PRIVATE);
        int index = prefs.getInt(getString(solversteam.madinagate.R.string.languages_key), 1);

        if (index == 0)  {
            Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/ae_AlArabiya.ttf");
            about.setTypeface(custom_font);
            contact.setTypeface(custom_font);
        } else if (index == 3) {
            Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/alvi_Nastaleeq_Lahori_shipped.ttf");
            about.setTypeface(custom_font);
            contact.setTypeface(custom_font);
        }

        about.setText(getResources().getStringArray(R.array.about_us)[index]);
        contact.setText(getResources().getStringArray(R.array.contact_us)[index]);
        // preparing list data
        prepareMenuListData();

    }

    private void prepareMenuListData() {

        connectionDetector=new ConnectionDetector(this);

        if(connectionDetector.isConnectingToInternet()) {
            connection = new Connection(this, "/GetAllMadinaLanguages/" + 1, "Get");
            connection.reset();
            connection.Connect(new Connection.Result() {
                @Override
                public void data(String str) throws JSONException {
                    Log.e("checkresponseresult",str);

                    menuListDataHeader = new ArrayList<String>();
                    menuListDataChild = new HashMap<String, List<String>>();

                    JSONObject jsonObject = new JSONObject(str);
                    JSONArray jsonArray = jsonObject.getJSONArray("1-languages");
                    List<String> languages = new ArrayList<String>();
                    String lan_title = "Language";
                    SharedPreferences prefs = getSharedPreferences(getString(solversteam.madinagate.R.string.languages_file_key), MODE_PRIVATE);
                    int index = prefs.getInt(getString(solversteam.madinagate.R.string.languages_key), 1);

                    for(int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = (JSONObject)jsonArray.get(i);

                        if (i == index)
                            lan_title = (String)object.get("sitename");
                        languages.add((String)object.get("title"));
                    }

                    menuListDataHeader.add(lan_title);
                    menuListDataChild.put(menuListDataHeader.get(0), languages);


                    // Adding child data
                    for(int i = 0; i < data.size(); i++) {
                        menuListDataHeader.add(data.get(i).getTitle());
                        menuListDataChild.put(data.get(i).getTitle(), data.get(i).getSubTilte());
                    }



                    setData ();
                }
            });

        }
        else {
            Utility.connectionToast(this);
        }

    }

    private void setData (){
        menuListAdapter = new ExpandableListAdapter(MainMenuActivity.this, menuListDataHeader, menuListDataChild);

        // setting list adapter
        menuExpListView.setAdapter(menuListAdapter);
        menuExpListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                if(i == 0){
                    SharedPreferences prefs = getSharedPreferences(getString(solversteam.madinagate.R.string.languages_file_key)
                            , MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt(getString(solversteam.madinagate.R.string.languages_key), i1);
                    editor.commit();

                    Intent intent = new Intent( MainMenuActivity.this, Splash.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else {
                    Intent intent = TopicActivity.getIntent(MainMenuActivity.this, data.get(i - 1));
                    MainMenuActivity.this.startActivity(intent);
                }

                return true;
            }
        });
    }

    private void init(){

        connectionDetector=new ConnectionDetector(this);

        if(connectionDetector.isConnectingToInternet()) {
            SharedPreferences prefs = getSharedPreferences(getString(solversteam.madinagate.R.string.languages_file_key), MODE_PRIVATE);
            int index = prefs.getInt(getString(solversteam.madinagate.R.string.languages_key), 1);
            connection = new Connection(this, "/GetAllMadinaHomeTopCategoryandsubcategory/"+ Constants.LANGUAGES[index]+"/0", "Get");
            connection.reset();
            connection.Connect(new Connection.Result() {
                @Override
                public void data(String str) throws JSONException {

                    ImageView view = (ImageView) findViewById(R.id.footer);

                    Log.e("checkresponseresult", str);

                    JSONObject jsonObject = new JSONObject(str);
                    JSONArray array = jsonObject.names();

                    for (int i = 0; i < array.length(); i++) {
                        String key = (String) array.get(i);
                        ArrayList<String> supCatList = new ArrayList<>();
                        ArrayList<Integer> catIds = new ArrayList<Integer>();
                        JSONArray supCat = jsonObject.getJSONArray(key);
                        for (int j = 0; j < supCat.length(); j++) {
                            JSONObject temp = (JSONObject) supCat.get(j);
                            supCatList.add(temp.getString("category_name"));
                            catIds.add(temp.getInt("id_category"));
                        }
                        data.add(new Section(key, supCatList, catIds));
                    }

                    initMenu();

                }
            });
        }
    }

    protected void updateViews(String languageCode) {
        Log.e("updateViews", "1");
        Log.e("HomeActivity : ", "Lang code = " + languageCode);
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        initToolbar();
        //initMenu();
        init();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(solversteam.madinagate.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        DrawerLayout drawer = (DrawerLayout) findViewById(solversteam.madinagate.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                dialog = new Dialog(this, R.style.CircularProgress);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_loading_item);
                dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);

                LinearLayout relativeLayout;
                relativeLayout=(LinearLayout) dialog.findViewById(R.id.rel_loder);
                relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View view, MotionEvent event) {
                        switch(event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                // The user just touched the screen
                                //   Toast.makeText(SplashActivity.this,"ended", Toast.LENGTH_SHORT).show();

                                dialog.dismiss();
                                break;
                            case MotionEvent.ACTION_UP:
                                // The touch just ended
                                // Toast.makeText(SplashActivity.this,"ended", Toast.LENGTH_SHORT).show();

                                break;
                        }

                        return false;
                    }
                });


                return dialog;
            default:
                return null;
        }
    }

    public boolean onTouchEvent(MotionEvent event)
    {

        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            System.out.println("TOuch outside the dialog ******************** ");
            dialog.dismiss();
        }
        return false;
    }
}
