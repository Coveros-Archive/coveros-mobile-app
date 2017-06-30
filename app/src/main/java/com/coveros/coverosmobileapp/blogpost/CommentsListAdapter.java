package com.coveros.coverosmobileapp.blogpost;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
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
        holder.getCommentName().setText(comment.getAuthor());
        holder.getCommentDate().setText(comment.getDate());
        holder.getCommentContent().setText(Html.fromHtml(comment.getContent(), Html.FROM_HTML_MODE_COMPACT));

        return row;
    }

    /**
     * Construct to contain TextViews in getView()
     */
    static class CommentHolder {
        TextView commentName;
        TextView commentDate;
        TextView commentContent;

        TextView getCommentName() {
            return commentName;
        }

        TextView getCommentDate() {
            return commentDate;
        }

        TextView getCommentContent() {
            return commentContent;
        }

    }
}
