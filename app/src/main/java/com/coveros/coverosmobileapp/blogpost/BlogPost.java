package com.coveros.coverosmobileapp.blogpost;

import android.util.Log;
import android.util.SparseArray;

import com.google.gson.JsonObject;

import org.apache.commons.text.StringEscapeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Represents a blog post.
 *
 * @author Maria Kim
 */
class BlogPost {

    private int id;
    private String date;
    private String content;
    private String title;
    private String authorDate;

    /**
     * Create a blog BlogPost instance
     * @param blogPostJson JsonObject that contains information about BlogPost received from Wordpress
     * @param authors    SparseArray that contains ids and names of all visible authors
     */
    BlogPost(JsonObject blogPostJson, SparseArray<String> authors) {
        this.id = blogPostJson.get("id").getAsInt();
        try {
            this.date = formatDate(blogPostJson.get("date").getAsString());
        } catch (ParseException e) {
            Log.e("Parse exception", e.toString());
        }
        String author = StringEscapeUtils.unescapeHtml4(authors.get(blogPostJson.get("author").getAsInt()));

        // to show in BlogPostListActivity
        this.title = StringEscapeUtils.unescapeHtml4(blogPostJson.get("title").getAsJsonObject().get("rendered").getAsString());
        authorDate = StringEscapeUtils.unescapeHtml4(author + "\n" + this.date);

        // to display in BlogPostReadActivity
        this.content = "<h3>" + this.title + "</h3><h4>" + author + "</h4><h5>" + this.date + "</h5>" + StringEscapeUtils.unescapeHtml4(blogPostJson.get("content").getAsJsonObject().get("rendered").getAsString());
    }

    /**
     * Modifies date from Wordpress to MMM dd YYYY format.
     *
     * @param date    the date to format
     * @return    the date in MM dd YYYY format
     * @throws ParseException    if the internal date format cannot be parsed
     */
    static String formatDate(String date) throws ParseException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        Date parsedDate = dateFormatter.parse(date);
        SimpleDateFormat datePrint = new SimpleDateFormat("MMM d, yyyy");
        return datePrint.format(parsedDate);
    }

    int getId() {
        return id;
    }

    String getTitle() {
        return title;
    }

    String getAuthorDate() {
        return authorDate;
    }

    String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return String.format("Heading: %s%nSubheading: %s", title, authorDate);
    }
}

