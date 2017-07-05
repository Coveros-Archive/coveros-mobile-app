package com.coveros.coverosmobileapp.blogpost;

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
class BlogPostsListAdapter extends ArrayAdapter<BlogPost> {

    private final Context context;
    private final int layoutResourceId;
    private BlogPostHolder holder;


    BlogPostsListAdapter(Context context, int layoutResourceId, List<BlogPost> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new BlogPostHolder();
            holder.title = (TextView) row.findViewById(R.id.title);
            holder.authorDate = (TextView) row.findViewById(R.id.author_date);
            row.setTag(holder);
        } else {
            holder = (BlogPostHolder) row.getTag();
        }

        BlogPost blogPost = getItem(position);
        holder.title.setText(blogPost.getTitle());
        holder.authorDate.setText(blogPost.getAuthorDate());

        return row;
    }

    BlogPostHolder getBlogPostHolder() {
        return holder;
    }

    static class BlogPostHolder {
        TextView title;
        TextView authorDate;
    }



}
