package com.coveros.coverosmobileapp.blogpost;

import android.util.Log;

import com.google.gson.JsonObject;

import org.apache.commons.text.StringEscapeUtils;

import java.text.ParseException;

/**
 * Represents a comment for a blog post.
 *
 * @author Maria Kim
 */

public class Comment {

    private String author;
    private String date;
    private String content;

    public Comment(JsonObject commentJson) {
        this.author = StringEscapeUtils.unescapeHtml4(commentJson.get("author_name").getAsString());
        try {
            this.date = BlogPost.formatDate(commentJson.get("date").getAsString());
        } catch (ParseException e) {
            this.date = "Unknown date";
            Log.e("Parse exception", e.toString());

        }
        this.content = StringEscapeUtils.unescapeHtml4(commentJson.get("content").getAsJsonObject().get("rendered").getAsString());
    }

    String getAuthor() {
        return author;
    }

    String getDate() {
        return date;
    }

    String getContent() {
        return content;
    }


}
