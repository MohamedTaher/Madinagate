package solversteam.madinagate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import solversteam.madinagate.R;
import solversteam.madinagate.data.Constants;
import solversteam.madinagate.data.connection.Connection;
import solversteam.madinagate.data.connection.ConnectionDetector;
import solversteam.madinagate.helper.Utility;
import solversteam.madinagate.model.Post;
import solversteam.madinagate.model.Section;
import solversteam.madinagate.ui.adabter.TopicAdapter;
import solversteam.madinagate.ui.listener.EndlessScrollListener;
import solversteam.madinagate.ui.listener.RecyclerItemClickListener;

public class TopicDetails extends MainMenuActivity {

    private int postID, postType;
    private TextView title;
    private WebView description;
    private ArrayList<Post> relatedData = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        postID = intent.getIntExtra("POST_ID",-1);

        relatedData.add((Post) intent.getSerializableExtra("RELATED_ONE"));
        relatedData.add((Post) intent.getSerializableExtra("RELATED_TWO"));
        relatedData.add((Post) intent.getSerializableExtra("RELATED_THREE"));
        relatedData.add((Post) intent.getSerializableExtra("RELATED_FOUR"));

        postType = intent.getIntExtra("POST_TYPE", -1);

        //updateViews(language_code);
        init();
    }

    public static Intent getIntent(Context context, int postId, int postType, Post p1, Post p2, Post p3, Post p4){
        Intent intent = new Intent(context, TopicDetails.class);
        intent.putExtra("POST_ID", postId);
        intent.putExtra("RELATED_ONE", p1);
        intent.putExtra("RELATED_TWO", p2);
        intent.putExtra("RELATED_THREE", p3);
        intent.putExtra("RELATED_FOUR", p4);
        intent.putExtra("POST_TYPE",postType);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    private void init(){

        final View contentView = getLayoutInflater().inflate(R.layout.activity_topic_details, null);
        drawer.addView(contentView, 0);



        if(connectionDetector.isConnectingToInternet()) {
            SharedPreferences prefs = getSharedPreferences(getString(solversteam.madinagate.R.string.languages_file_key), MODE_PRIVATE);
            int index = prefs.getInt(getString(solversteam.madinagate.R.string.languages_key), 1);

            connection = new Connection(this, "/GetAllmadinapostdata/"+ Constants.LANGUAGES[index]+"/" + postID + "/" + postType, "Get");
            connection.reset();
            connection.Connect(new Connection.Result() {
                @Override
                public void data(String str) throws JSONException {

                    title = (TextView) contentView.findViewById(R.id.title);
                    description = (WebView) contentView.findViewById(R.id.description);

                    SharedPreferences prefs = getSharedPreferences(getString(solversteam.madinagate.R.string.languages_file_key), MODE_PRIVATE);
                    int index = prefs.getInt(getString(solversteam.madinagate.R.string.languages_key), 1);
                    if (index == 0)  {
                        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/ae_AlArabiya.ttf");
                        title.setTypeface(custom_font);
                    } else if (index == 3) {
                        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/alvi_Nastaleeq_Lahori_shipped.ttf");
                        title.setTypeface(custom_font);
                    }



                    Log.e("checkresponseresult", str);
                    JSONArray array = new JSONObject(str).getJSONArray("postdata");
                    JSONObject object = array.getJSONObject(0);

                    title.setText(object.getString("title"));
                    description.getSettings().setJavaScriptEnabled(true);
                    String des = object.getString("fulltext");
                    if (des == null || des.equals(""))
                        des = object.getString("introtext");
                    description.loadDataWithBaseURL("", "<html dir=\"rtl\" lang=\"\"><body>" + des + "</body></html>", "text/html", "UTF-8",null);

                    initRelated();

                }
            });
        } else {
            Utility.connectionToast(this);
        }
    }

    private void initRelated(){
        final TextView related = (TextView) findViewById(R.id.related_text);
        related.setVisibility(View.VISIBLE);
        SharedPreferences prefs = getSharedPreferences(getString(solversteam.madinagate.R.string.languages_file_key), MODE_PRIVATE);
        int index = prefs.getInt(getString(solversteam.madinagate.R.string.languages_key), 1);
        if (index == 0)  {
            Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/ae_AlArabiya.ttf");
            related.setTypeface(custom_font);
        } else if (index == 3) {
            Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/alvi_Nastaleeq_Lahori_shipped.ttf");
            related.setTypeface(custom_font);
        }

        for (int i = 0; i < relatedData.size(); i++) {
            if (relatedData.get(i) == null) {
                relatedData.remove(i);
                i--;
            }
        }

        if(relatedData.size() == 0)related.setVisibility(View.GONE);

        related.setText(getResources().getStringArray(R.array.related)[index]);


        mRecyclerView = (RecyclerView) findViewById(R.id.related_list);

        //list start
        mRecyclerView.setHasFixedSize(true);

        //mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {



                        startActivity(TopicDetails.getIntent(
                                TopicDetails.this
                                ,relatedData.get(position).getId()
                                ,relatedData.get(position).getPosttype()
                                ,relatedData.get(0)
                                ,relatedData.get(1)
                                ,relatedData.get(2)
                                ,relatedData.get(3)
                                )
                        );

                        //SearchActivity.this.startActivity(TopicDetails.getIntent(SearchActivity.this,(data.get(position).getId())));
                    }
                })
        );


        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new TopicAdapter(TopicDetails.this, relatedData, 0, new Section("",new ArrayList<String>(),new ArrayList<Integer>())
                , new EndlessScrollListener() {
            @Override
            public void action() {
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        //list end

    }

}
