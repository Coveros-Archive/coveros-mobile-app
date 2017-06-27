package com.coveros.coverosmobileapp.blogpost;

import android.util.Log;

import com.google.gson.JsonObject;
import org.apache.commons.text.StringEscapeUtils;
import java.text.ParseException;

/**
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
        } catch(ParseException e) {
            Log.e("Parse exception", e.toString());

        }
        this.content = StringEscapeUtils.unescapeHtml4(commentJson.get("content").getAsJsonObject().get("rendered").getAsString());
    }

    // for adding example comments for development purposes
    public Comment(String author, String date, String content) {
        this.author = StringEscapeUtils.unescapeHtml4(author);
        try {
            this.date = BlogPost.formatDate(date);
        } catch(ParseException e) {
            Log.e("Parse exception", e.toString());

        }
        this.content = StringEscapeUtils.unescapeHtml4(content);
    }

    String getAuthor() { return author; }
    String getDate() { return date; }
    String getContent() { return content; }



}
