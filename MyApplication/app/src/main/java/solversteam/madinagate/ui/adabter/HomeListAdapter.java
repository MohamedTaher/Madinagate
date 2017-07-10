package solversteam.madinagate.ui.adabter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import solversteam.madinagate.ui.activity.TopicActivity;
import solversteam.madinagate.model.Section;

import solversteam.madinagate.R;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by taher on 4/25/17.
 */

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {

    private ArrayList<Section> data;
    private Context context;

    public HomeListAdapter(Context context, ArrayList<Section> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int _position = position;

        holder.title.setText(data.get(_position).getTitle());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent intent = new Intent(context, TopicActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);*/

                context.startActivity(TopicActivity.getIntent(context,data.get(_position)));
            }
        });

        String[] values = new String[data.get(_position).getSubTilte().size()];
        for(int i = 0; i < data.get(_position).getSubTilte().size(); i++) {
            values[i] = data.get(_position).getSubTilte().get(i);
        }

/*
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                R.layout.sup_title_layout, android.R.id.text1, values);
*/
        supTitleAdapter adapter = new supTitleAdapter(context, R.layout.sup_title_layout, values);

        holder.subTitle.setAdapter(adapter);

        holder.subTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Section section = new Section(
                        data.get(_position).getCategoryIDs().get(i) + "-" + data.get(_position).getSubTilte().get(i)
                        ,new ArrayList<String>()
                        , new ArrayList<Integer>()
                );

                context.startActivity(TopicActivity.getIntent(context,section));
            }
        });

        holder.subTitle.setExpanded(true);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ExpandableHeightListView subTitle;

        public ViewHolder(View v) {
            super(v);

            title = (TextView) v.findViewById(R.id.title);

            SharedPreferences prefs = context.getSharedPreferences(context.getString(solversteam.madinagate.R.string.languages_file_key), context.MODE_PRIVATE);
            int index = prefs.getInt(context.getString(solversteam.madinagate.R.string.languages_key), 1);

            if (index == 0)  {
                Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/ae_AlArabiya.ttf");
                title.setTypeface(custom_font);
            } else if (index == 3) {
                Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/alvi_Nastaleeq_Lahori_shipped.ttf");
                title.setTypeface(custom_font);
            }
            subTitle = (ExpandableHeightListView) v.findViewById(R.id.sub_title);
        }
    }
}
