package solversteam.madinagate.ui.adabter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import solversteam.madinagate.R;
import solversteam.madinagate.helper.GetScreenSize;
import solversteam.madinagate.helper.Utility;
import solversteam.madinagate.model.Post;
import solversteam.madinagate.model.Section;
import solversteam.madinagate.ui.activity.TopicActivity;
import solversteam.madinagate.ui.activity.TopicDetails;
import solversteam.madinagate.ui.listener.EndlessScrollListener;
import solversteam.madinagate.ui.listener.ListenerHandler;

import java.util.ArrayList;

/**
 * Created by root on 4/26/17.
 */

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {

    private static final int ITEM_ONE = 1;
    private static final int ITEM_TWO = 2;

    private Context context;
    private ListenerHandler listenerHandler;
    private ArrayList<Post> data;
    private int cat_sz;
    private Section section;
    private EndlessScrollListener listener;

    private int height,width;

    public TopicAdapter (Context context, ArrayList<Post> data, int cat_sz, Section section, EndlessScrollListener listener){
        this.context = context;
        this.data = data;
        this.cat_sz = cat_sz;
        this.section = section;
        this.listener = listener;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) == null) {
                data.remove(i);
                i--;
            }
        }

        GetScreenSize getScreenSize=new GetScreenSize(context);
        getScreenSize.getImageSize();
        height=getScreenSize.getHeight();
        width=getScreenSize.getWidth();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout = viewType  == ITEM_ONE ? solversteam.madinagate.R.layout.file_layout : solversteam.madinagate.R.layout.topic_item_layout;

        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

        SharedPreferences prefs = context.getSharedPreferences(context.getString(solversteam.madinagate.R.string.languages_file_key), context.MODE_PRIVATE);
        int index = prefs.getInt(context.getString(solversteam.madinagate.R.string.languages_key), 1);

        View v = holder.view;
        if (getItemViewType(position) == ITEM_ONE){
            holder.title = (TextView) v.findViewById(R.id.title);
            holder.title.setText(section.getSubTilte().get(position));

        } else {

            holder.title = (TextView) v.findViewById(R.id.title);
            //todo post is null object
            holder.title.setText(data.get(position - cat_sz).getTitle());

            holder.description = (TextView) v.findViewById(R.id.description);
            holder.description.setText(Html.fromHtml(data.get(position - cat_sz).getIntrotext()));

            if(position >= data.size() + cat_sz -1) {
                listener.action();
            }

            if (index == 0)  {
                Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/ae_AlArabiya.ttf");
                holder.description.setTypeface(custom_font);
            } else if (index == 3) {
                Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/alvi_Nastaleeq_Lahori_shipped.ttf");
                holder.description.setTypeface(custom_font);
            }
        }


        if (index == 0)  {
            Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/ae_AlArabiya.ttf");
            holder.title.setTypeface(custom_font);
        } else if (index == 3) {
            Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/alvi_Nastaleeq_Lahori_shipped.ttf");
            holder.title.setTypeface(custom_font);
        }


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position > cat_sz - 1 ) {

                    ArrayList<Integer> rand = Utility.generateDistinctRandomNumbers(4, 0, data.size());

                    context.startActivity(TopicDetails.getIntent(
                            context
                            ,data.get(position - cat_sz).getId()
                            ,data.get(position - cat_sz).getPosttype()
                            ,data.get(rand.get(0))
                            ,data.get(rand.get(1))
                            ,data.get(rand.get(2))
                            ,data.get(rand.get(3))
                    )
                    );
                } else {

                    Section _section = new Section(
                            section.getCategoryIDs().get(position) + "-" + section.getSubTilte().get(position)
                            ,new ArrayList<String>()
                            , new ArrayList<Integer>()
                    );


                    context.startActivity(TopicActivity.getIntent(context,_section));
                }
            }
        });

        if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams sglp = (StaggeredGridLayoutManager.LayoutParams) lp;
            sglp.setFullSpan(position > cat_sz - 1 ? true:false);
            holder.itemView.setLayoutParams(sglp);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < cat_sz) {
            return ITEM_ONE;
        } else {
            return ITEM_TWO;
        }

    }

    @Override
    public int getItemCount() {
        return data.size() + section.getSubTilte().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout layout;
        public View view;
        public TextView title, description;

        public ViewHolder(View v) {
            super(v);

            view = v;
            layout = (RelativeLayout) v.findViewById(R.id.file_layout_id);
/*
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();
            params.height = width/3;
            layout.setLayoutParams(params);*/

        }
    }

}
