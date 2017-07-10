package solversteam.madinagate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import solversteam.madinagate.R;
import solversteam.madinagate.data.Constants;
import solversteam.madinagate.data.connection.Connection;
import solversteam.madinagate.helper.Utility;
import solversteam.madinagate.model.Post;
import solversteam.madinagate.model.Section;
import solversteam.madinagate.ui.adabter.TopicAdapter;
import solversteam.madinagate.ui.listener.EndlessScrollListener;
import solversteam.madinagate.ui.listener.RecyclerItemClickListener;

import java.util.ArrayList;

public class TopicActivity  extends MainMenuActivity {

    private RecyclerView mRecyclerView ;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView noRes;
    private LinearLayout title;
    private Section section;
    private ArrayList<Post> data = new ArrayList<>();;
    private int pageNum = 0;
    private final int pageSize = 10;
    private boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(solversteam.madinagate.R.layout.activity_topic);

        Intent intent = getIntent();
        section = (Section) intent.getSerializableExtra("SECTION");


        View contentView = getLayoutInflater().inflate(R.layout.activity_topic, null, false);
        drawer.addView(contentView, 0);


        noRes = (TextView) findViewById(R.id.no_res);
        final TextView title = (TextView) contentView.findViewById(R.id.title);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        SharedPreferences prefs = getSharedPreferences(getString(solversteam.madinagate.R.string.languages_file_key), MODE_PRIVATE);
        int index = prefs.getInt(getString(solversteam.madinagate.R.string.languages_key), 1);

        if (index == 0)  {
            Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/ae_AlArabiya.ttf");
            title.setTypeface(custom_font);
            noRes.setTypeface(custom_font);
        } else if (index == 3) {
            Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/alvi_Nastaleeq_Lahori_shipped.ttf");
            title.setTypeface(custom_font);
            noRes.setTypeface(custom_font);
        }
        title.setText(section.getTitle());
        noRes.setText(getResources().getStringArray(R.array.noRes)[index]);


        mRecyclerView = (RecyclerView) findViewById(solversteam.madinagate.R.id.topic_list);
        data = new ArrayList<>();
        mAdapter = new TopicAdapter(TopicActivity.this, data, section.getSubTilte().size(), section
                , new EndlessScrollListener() {
            @Override
            public void action() {
                if (loading) init();
            }
        });
        mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(TopicActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                })
        );
        init();
    }

    public static Intent getIntent(Context context, Section section) {

        Intent intent = new Intent(context,TopicActivity.class);
        intent.putExtra("SECTION", section);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    private void init() {


        if(connectionDetector.isConnectingToInternet()) {
            SharedPreferences prefs = getSharedPreferences(getString(solversteam.madinagate.R.string.languages_file_key), MODE_PRIVATE);
            int index = prefs.getInt(getString(solversteam.madinagate.R.string.languages_key), 1);

            connection = new Connection(this, "/GetAllMadinaPosts/"+ Constants.LANGUAGES[index] +"/"+section.getID()+"/"+ pageNum++ +"/" + pageSize, "Get");
            connection.reset();
            connection.Connect(new Connection.Result() {
                @Override
                public void data(String str) throws JSONException {

                    Log.e("checkresponseresult", str);
                    JSONArray array = new JSONArray(str);

                    if (array.length() + section.getSubTilte().size() == 0)noRes.setVisibility(View.VISIBLE);

                    if (array.length() < pageSize) loading = false;

                    for(int i = 0; i < array.length(); i++) {
                        Gson gson = new GsonBuilder().create();
                        JSONObject jo = array.getJSONObject(i);
                        Post post = gson.fromJson(jo.toString(), Post.class);

                        data.add(post);
                    }


                    mAdapter.notifyDataSetChanged();




                }
            });
        } else {
            Utility.connectionToast(this);
        }



    }
}
