package solversteam.madinagate.ui.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import solversteam.madinagate.R;
import solversteam.madinagate.data.Constants;
import solversteam.madinagate.data.connection.Connection;
import solversteam.madinagate.data.connection.ConnectionDetector;
import solversteam.madinagate.helper.Utility;
import solversteam.madinagate.model.Post;
import solversteam.madinagate.model.Section;
import solversteam.madinagate.ui.adabter.TopicAdapter;
import solversteam.madinagate.ui.listener.EndlessScrollListener;
import solversteam.madinagate.ui.listener.ListenerHandler;
import solversteam.madinagate.ui.listener.RecyclerItemClickListener;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private Dialog dialog;
    private TextView noResult;

    private Connection connection;
    private ConnectionDetector connectionDetector;

    private int startpage = 0;
    private final int newscount = 10;

    private ArrayList<Post> data = new ArrayList<>();
    private String queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setTitle(getString(R.string.search));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        connectionDetector=new ConnectionDetector(this);
        init();
    }

    private void init() {

        noResult = (TextView) findViewById(R.id.no_res);
        SharedPreferences prefs = getSharedPreferences(getString(solversteam.madinagate.R.string.languages_file_key), MODE_PRIVATE);
        int index = prefs.getInt(getString(solversteam.madinagate.R.string.languages_key), 1);
        noResult.setText(getResources().getStringArray(R.array.noRes)[index]);
        mRecyclerView = (RecyclerView) findViewById(R.id.search_list);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        data = new ArrayList<>();
        if(query != null || !query.equals("")) {

            this.queue = query;
            data = new ArrayList<>();
            searchAndSetResult(query);


            return true;
        }

        return false;
    }

    private void searchAndSetResult(String text) {
        connectionDetector=new ConnectionDetector(this);
        if(connectionDetector.isConnectingToInternet()) {

            SharedPreferences prefs = getSharedPreferences(getString(solversteam.madinagate.R.string.languages_file_key), MODE_PRIVATE);
            final int index = prefs.getInt(getString(solversteam.madinagate.R.string.languages_key), 1);
            int id = Constants.LANGUAGES[index];

            connection = new Connection(this, "/GetAllMadinaPostsBySearch/"+ id +"/" + text , "Get");
            connection.reset();
            connection.Connect(new Connection.Result() {
                @Override
                public void data(String str) throws JSONException {
                    Log.e("checkresponseresult",str);
                    startpage++;

                    JSONArray jsonArray = new JSONObject(str).getJSONArray("1-PostsBySearch");

                    if (jsonArray.length() == 0) {
                        noResult.setVisibility(View.VISIBLE);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        Gson gson = new GsonBuilder().create();
                        Post post = gson.fromJson(jo.toString(), Post.class);
                        data.add(post);
                    }

                    setData();

                    if (data.size() == 0){

                        Toast.makeText(SearchActivity.this, SearchActivity.this.getResources().getStringArray(R.array.no_result)[index], Toast.LENGTH_LONG);
                    }

                }
            });

        }
        else {
            Utility.connectionToast(this);
        }
    }

    private void setData() {

        //list start
        mRecyclerView.setHasFixedSize(true);

        //mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        ArrayList<Integer> rand = Utility.generateDistinctRandomNumbers(4, 0, data.size());

                        startActivity(TopicDetails.getIntent(
                                SearchActivity.this
                                ,data.get(position).getId()
                                ,data.get(position).getPosttype()
                                ,data.get(rand.get(0))
                                ,data.get(rand.get(1))
                                ,data.get(rand.get(2))
                                ,data.get(rand.get(3))
                                )
                        );

                        //SearchActivity.this.startActivity(TopicDetails.getIntent(SearchActivity.this,(data.get(position).getId())));
                    }
                })
        );


        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new TopicAdapter(SearchActivity.this, data, 0, new Section("",new ArrayList<String>(), new ArrayList<Integer>())
                , new EndlessScrollListener() {
            @Override
            public void action() {
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        //list end
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.equals(queue) && mAdapter != null){
            while (data.size() != 0) data.remove(0);
            mAdapter.notifyDataSetChanged();
            noResult.setVisibility(View.INVISIBLE);
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            //noResult.setVisibility(View.INVISIBLE);
            return true;
        } else if (id == android.R.id.home){
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
