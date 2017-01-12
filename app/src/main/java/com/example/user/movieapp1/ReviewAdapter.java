package com.example.user.movieapp1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 11/25/2016.
 */
public class ReviewAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<ResponseReview.ResultsBean> review;
    ResponseReview.ResultsBean result;
    String content;
    String author;


    public ReviewAdapter (Context c ,ArrayList<ResponseReview.ResultsBean> review ){
        mContext = c;
        this.review=review;
    }
    public int getCount() {
        return review.size();

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder= new ViewHolder();
        View v=convertView;
        if (v == null || holder == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(
                    R.layout.review_list_item, parent, false);
            holder.authertv =(TextView)v.findViewById(R.id.list_item_author_textview);
            holder.contenttv =(TextView)v.findViewById(R.id.list_item_review_textview);
            v.setTag(holder);

        }else {
            holder = (ViewHolder)v.getTag();
        }

            result = review.get(position);
            content=result.getContent().toString();
            author=result.getAuthor().toString();
        holder.contenttv.setText(content);
        holder.authertv.setText(author);


        return v;
    }

}
class ViewHolder{
    TextView authertv;
    TextView contenttv;
}