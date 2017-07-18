package com.coveros.coverosmobileapp.blogpost;

import android.util.Log;

import com.google.gson.JsonObject;

import org.apache.commons.text.StringEscapeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
           Date d = new SimpleDateFormat(BlogPostFactory.WORDPRESS_BLOGPOST_DATE_FORMAT).parse(commentJson.get("date").getAsString());
            this.date = new SimpleDateFormat(BlogPostHtmlDecorator.HTML_DATE_FORMAT_STRING).format(d);

        } catch (ParseException e) {
            this.date = "";
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
