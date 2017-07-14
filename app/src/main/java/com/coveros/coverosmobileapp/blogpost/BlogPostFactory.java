package com.coveros.coverosmobileapp.blogpost;

import android.util.SparseArray;

import com.google.gson.JsonObject;

import org.apache.commons.text.StringEscapeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by brian on 7/13/17.
 */
public class BlogPostFactory {

    public static final String WORDPRESS_BLOGPOST_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * Create a blog post from JSON
     *
     * @param blogPostJson
     * @param authors      list of author/id pairs used to look up the author name based on id specified in the JSON
     * @return
     */
    public BlogPost createBlogPost(JsonObject blogPostJson, SparseArray<String> authors) {
        Date date = null;
        try {
            date = new SimpleDateFormat(WORDPRESS_BLOGPOST_DATE_FORMAT).parse(blogPostJson.get("date").getAsString());
        } catch (ParseException e) {
            throw new InvalidBlogPostJsonDateException(e);
        }
        String title = parseBlogObject(blogPostJson);
        int authorId = blogPostJson.get("author").getAsInt();
        String author = StringEscapeUtils.unescapeHtml4(authors.get(authorId));
        String content = "<h3>" + title + "</h3><h4>" + author + "</h4><h5>" + date + "</h5>" + StringEscapeUtils.unescapeHtml4(blogPostJson.get("content").getAsJsonObject().get("rendered").getAsString());
        int id = blogPostJson.get("id").getAsInt();

        return new BlogPost(id, date, content, title, author);
    }


    private String parseBlogObject(JsonObject json) {
        return StringEscapeUtils.unescapeHtml4(json.get("title").getAsJsonObject().get("rendered").getAsString());
    }


}
