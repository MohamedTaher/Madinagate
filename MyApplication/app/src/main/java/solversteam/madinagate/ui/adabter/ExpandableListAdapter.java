package solversteam.madinagate.ui.adabter;

/**
 * Created by root on 4/26/17.
 */


import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import solversteam.madinagate.app.MyApplication;
import solversteam.madinagate.ui.activity.SettingActivity;
import solversteam.madinagate.ui.activity.Splash;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {

        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(solversteam.madinagate.R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(solversteam.madinagate.R.id.lblListItem);

        SharedPreferences prefs = MyApplication.getContext().getSharedPreferences(MyApplication.getContext().getString(solversteam.madinagate.R.string.languages_file_key), MyApplication.getContext().MODE_PRIVATE);
        int index = prefs.getInt(MyApplication.getContext().getString(solversteam.madinagate.R.string.languages_key), 1);

        if (index == 0)  {
            Typeface custom_font = Typeface.createFromAsset(MyApplication.getContext().getAssets(),  "fonts/ae_AlArabiya.ttf");
            txtListChild.setTypeface(custom_font);
        } else if (index == 3) {
            Typeface custom_font = Typeface.createFromAsset(MyApplication.getContext().getAssets(),  "fonts/alvi_Nastaleeq_Lahori_shipped.ttf");
            txtListChild.setTypeface(custom_font);
        }
        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(solversteam.madinagate.R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(solversteam.madinagate.R.id.lblListHeader);
        SharedPreferences prefs = MyApplication.getContext().getSharedPreferences(MyApplication.getContext().getString(solversteam.madinagate.R.string.languages_file_key), MyApplication.getContext().MODE_PRIVATE);
        int index = prefs.getInt(MyApplication.getContext().getString(solversteam.madinagate.R.string.languages_key), 1);

        if (index == 0)  {
            Typeface custom_font = Typeface.createFromAsset(MyApplication.getContext().getAssets(),  "fonts/ae_AlArabiya.ttf");
            lblListHeader.setTypeface(custom_font);
        } else if (index == 3) {
            Typeface custom_font = Typeface.createFromAsset(MyApplication.getContext().getAssets(),  "fonts/alvi_Nastaleeq_Lahori_shipped.ttf");
            lblListHeader.setTypeface(custom_font);
        }

        //lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}