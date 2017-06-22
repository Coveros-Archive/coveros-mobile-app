package com.coveros.coverosmobileapp.post;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.coveros.coverosmobileapp.R;

import java.util.List;

/**
 * Custom adapter to handle multi-line items in the postListView.
 * @author Maria Kim
 */
class PostListAdapter extends ArrayAdapter<Post> {

    private final Context context;
    private final int layoutResourceId;
    private final List<Post> data;

    PostListAdapter(Context context, int layoutResourceId, List<Post> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        PostViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new PostViewHolder();
            holder.heading = (TextView) row.findViewById(R.id.title);
            holder.subheading = (TextView) row.findViewById(R.id.author_date);
            row.setTag(holder);
        } else {
            holder = (PostViewHolder) row.getTag();
        }

        Post post = data.get(position);
        holder.heading.setText(post.getHeading());
        holder.subheading.setText(post.getSubheading());

        return row;
    }

    private static class PostViewHolder {
        TextView heading;
        TextView subheading;
    }
}
