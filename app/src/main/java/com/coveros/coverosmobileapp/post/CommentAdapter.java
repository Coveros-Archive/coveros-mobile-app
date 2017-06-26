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
 * @author Maria Kim
 */

class CommentAdapter extends ArrayAdapter<Comment> {

    private final Context context;
    private final int layoutResourceId;
    private final List<Comment> data;

    CommentAdapter(Context context, int layoutResourceId, List<Comment> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        CommentHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new CommentHolder();
            holder.commentName = (TextView) row.findViewById(R.id.comment_name);
            holder.commentDate = (TextView) row.findViewById(R.id.comment_date);
            holder.commentContent = (TextView) row.findViewById(R.id.comment_content);
            row.setTag(holder);
        } else {
            holder = (CommentHolder) row.getTag();
        }

        Comment comment = data.get(position);
        holder.commentName.setText(comment.getAuthor());
        holder.commentDate.setText(comment.getDate());
        holder.commentContent.setText(comment.getContent());

        return row;
    }

    private static class CommentHolder {
        TextView commentName;
        TextView commentDate;
        TextView commentContent;
    }
}
