package com.coveros.coverosmobileapp;

import android.util.Log;

import org.apache.commons.text.StringEscapeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Represents a blog post.
 * @author Maria Kim
 */
class Post {

    private String title;
    private String date;
    private String heading;
    private String subheading;
    private String content;
    private Author author;
    private int id;

    /**
     * Create a blog Post instance
     * @param title The title of the blog post
     * @param date The date the blog post was published
     * @param author The author of the blog post
     * @param id The id of the blog post
     * @param content The html content of the blog post
     */
    Post(String title, String date, Author author, int id, String content) {
        this.title = StringEscapeUtils.unescapeHtml4(title);
        try {
            this.date = formatDate(date);
        } catch (ParseException e) {
            Log.e("Parse exception", e.toString());
        }
        this.author = author;
        this.id = id;
        this.content = StringEscapeUtils.unescapeHtml4(content);

        heading = StringEscapeUtils.unescapeHtml4(this.title);
        subheading = StringEscapeUtils.unescapeHtml4(this.author + "\n" + this.date);
    }

    /**
     * Modifies date from Wordpress to MMM dd YYYY format.
     * @param date The date to format
     * @return The date in MM dd YYYY format
     * @throws ParseException If the internal date format cannot be parsed
     */
    protected String formatDate(String date) throws ParseException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        Date parsedDate = dateFormatter.parse(date);
        SimpleDateFormat datePrint = new SimpleDateFormat("MMM d, yyyy");
        return datePrint.format(parsedDate);
    }

    public String getTitle() { return title; }
    public int getId() { return id; }
    public String getHeading() { return heading; }
    public String getSubheading() { return subheading; }
    public String getContent() { return content; }

    public String toString() {
        return "Heading: " + heading + "\nSubheading: " + subheading;
    }
}

