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
 * Custom adapter to handle multi-line items in the postListView.
 * @author Maria Kim
 */
public class PostListAdapter extends ArrayAdapter<Post> {

    static Context context;
    static int layoutResourceId;
    List<Post> data = null;

    public PostListAdapter(Context context, int layoutResourceId, List<Post> data) {
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

        Post post = data.get(position);
        holder.heading.setText(post.getHeading());
        holder.subheading.setText(post.getSubheading());

        return row;

    }

    static class PostViewHolder {
        TextView heading;
        TextView subheading;
    }
}
