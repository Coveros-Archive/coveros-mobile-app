package com.coveros.coverosmobileapp.post;

import android.util.Log;

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
class Post {
    private String title;
    private String date;
    private String heading;
    private String subheading;
    private String content;
    private int id;

    /**
     * Create a blog Post instance
     *
     * @param title   The title of the blog post
     * @param date    The date the blog post was published
     * @param author  The author of the blog post
     * @param id      The id of the blog post
     * @param content The html content of the blog post
     */
    Post(String title, String date, String author, int id, String content) {
        this.title = StringEscapeUtils.unescapeHtml4(title);

        try {
            this.date = formatDate(date);
        } catch (ParseException e) {
            Log.e("Parse exception", e.toString());
        }

        this.id = id;


        heading = StringEscapeUtils.unescapeHtml4(this.title);
        subheading = StringEscapeUtils.unescapeHtml4(author + "\n" + this.date);
        this.content = "<h3>" + heading + "</h3><h4>" + author + "</h4><h5>" + this.date + "</h5>" + StringEscapeUtils.unescapeHtml4(content);
    }

    /**
     * Modifies date from Wordpress to MMM dd YYYY format.
     *
     * @param date The date to format
     * @return The date in MM dd YYYY format
     * @throws ParseException If the internal date format cannot be parsed
     */
    static String formatDate(String date) throws ParseException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        Date parsedDate = dateFormatter.parse(date);
        SimpleDateFormat datePrint = new SimpleDateFormat("MMM d, yyyy");
        return datePrint.format(parsedDate);
    }

    String getTitle() {
        return title;
    }

    int getId() {
        return id;
    }

    String getHeading() {
        return heading;
    }

    String getSubheading() {
        return subheading;
    }

    String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return String.format("Heading: %s%nSubheading: %s", heading, subheading);
    }
}

