package com.example.user.movieapp1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 11/25/2016.
 */
public class TrailerAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<ResponseTrailer.ResultsBean> trailer;
    ResponseTrailer.ResultsBean result;
    String name;
    String key;
    LayoutInflater inflater;

    public TrailerAdapter (Context c ,ArrayList<ResponseTrailer.ResultsBean> trailer ){
        mContext = c;
        this.trailer=trailer;

    }
    @Override
    public int getCount() {
        return trailer.size();
    }

    @Override
    public Object getItem(int position) {
        return trailer.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder2 holder2  = new ViewHolder2();
        View v=convertView;
        if (v == null || holder2 == null) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.trailer_list_item, parent, false);
            holder2.btn = (ImageView)v.findViewById(R.id.iconbtn);
            holder2.tv = (TextView)v.findViewById(R.id.trailerTv);
            v.setTag(holder2);
        }else {
            holder2 = (ViewHolder2)v.getTag();
        }

        result = trailer.get(position);
        String x=result.getName();
        holder2.tv.setText(x);
        result.getKey();

        return v;
    }
}
class ViewHolder2{
    ImageView btn;
    TextView tv;
}
