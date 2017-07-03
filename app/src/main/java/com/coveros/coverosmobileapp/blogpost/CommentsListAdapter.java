package com.coveros.coverosmobileapp.blogpost;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.coveros.coverosmobileapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom adapter to handle multi-line items in the commentsListView.
 *
 * @author Maria Kim
 */

class CommentsListAdapter extends ArrayAdapter<Comment> {

    private final Context context;
    private final int layoutResourceId;
    private final List<Comment> data;
    private CommentHolder holder;


    CommentsListAdapter(Context context, int layoutResourceId, List<Comment> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = new ArrayList<>(data);
    }

    @Override
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new CommentHolder();
            holder.commentName = (TextView) row.findViewById(R.id.comment_name);
            holder.commentDate = (TextView) row.findViewById(R.id.comment_date);
            holder.commentContent = (TextView) row.findViewById(R.id.comment_content);
            row.setTag(holder);
            Log.d("CLADAPTER", "ROW IS NULL");
        } else {
            holder = (CommentHolder) row.getTag();
            Log.d("CLADAPTER", "ROW IS NOT NULL");
        }

        Comment comment = data.get(position);
        holder.commentName.setText(comment.getAuthor());
        holder.commentDate.setText(comment.getDate());
        holder.commentContent.setText(Html.fromHtml(comment.getContent(), Html.FROM_HTML_MODE_LEGACY));

        return row;
    }

    CommentHolder getCommentHolder() {
        return holder;
    }

    /**
     * Construct to contain TextViews in getView()
     */
    static class CommentHolder {
        TextView commentName;
        TextView commentDate;
        TextView commentContent;

    }
}
