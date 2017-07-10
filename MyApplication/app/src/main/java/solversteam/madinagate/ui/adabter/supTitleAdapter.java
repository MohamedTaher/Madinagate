package solversteam.madinagate.ui.adabter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import solversteam.madinagate.R;

/**
 * Created by root on 5/1/17.
 */

public class supTitleAdapter extends ArrayAdapter<String> {

    Context context;
    int layoutResourceId;
    String data[] = null;

    public supTitleAdapter(Context context, int layoutResourceId, String[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WeatherHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new WeatherHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.title);

            SharedPreferences prefs = context.getSharedPreferences(context.getString(solversteam.madinagate.R.string.languages_file_key), context.MODE_PRIVATE);
            int index = prefs.getInt(context.getString(solversteam.madinagate.R.string.languages_key), 1);

            if (index == 0)  {
                Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/ae_AlArabiya.ttf");
                holder.txtTitle.setTypeface(custom_font);
            } else if (index == 3) {
                Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/alvi_Nastaleeq_Lahori_shipped.ttf");
                holder.txtTitle.setTypeface(custom_font);
            }

            row.setTag(holder);
        }
        else
        {
            holder = (WeatherHolder)row.getTag();
        }

        holder.txtTitle.setText(data[position]);

        return row;
    }

    static class WeatherHolder
    {
        TextView txtTitle;
    }
}