package com.coveros.coverosmobileapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by MariaKim on 6/18/17.
 */

public class PostListAdapter extends ArrayAdapter<PostMetaData> {

    static Context context;
    static int layoutResourceId;
    List<PostMetaData> data = null;

    public PostListAdapter(Context context, int layoutResourceId, List<PostMetaData> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PostViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new PostViewHolder();
            holder.heading = (TextView) row.findViewById(R.id.title);
            holder.subheading = (TextView) row.findViewById(R.id.author_date);
            row.setTag(holder);
        }
        else {
            holder = (PostViewHolder) row.getTag();
        }

        PostMetaData post = data.get(position);

        return row;

    }

    static class PostViewHolder {
        TextView heading;
        TextView subheading;
    }
}
