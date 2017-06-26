package com.coveros.coverosmobileapp.post;

import android.text.Html;
import android.util.Log;

import org.apache.commons.text.StringEscapeUtils;

import java.text.ParseException;

/**
 * @author Maria Kim
 */

public class Comment {

    private String author;
    private String date;
    private String content;

    public Comment(String author, String date, String content) {
        this.author = StringEscapeUtils.unescapeHtml4(author);
        try {
            this.date = Post.formatDate(date);
        } catch(ParseException e) {
            Log.e("Parse exception", e.toString());

        }
        this.content = StringEscapeUtils.unescapeHtml4(content);
    }

    String getAuthor() { return author; }
    String getDate() { return date; }
    String getContent() { return content; }



}
