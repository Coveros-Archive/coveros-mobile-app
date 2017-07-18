package com.coveros.coverosmobileapp.blogpost;

import org.apache.commons.text.StringEscapeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Decorates a blog post and ensures it is able to be displayed properly in an HTML page.
 * <p
 * Created by brian on 7/17/17.
 */

public class BlogPostHtmlDecorator implements BlogPostItem {

    public static final String HTML_DATE_FORMAT_STRING = "MMM d, yyyy";

    private BlogPostItem blogPostToDecorate;

    private String htmlDateString;


    BlogPostHtmlDecorator(BlogPostItem blogPostToDecorate) {
        this.blogPostToDecorate = blogPostToDecorate;
        SimpleDateFormat datePrint = new SimpleDateFormat(HTML_DATE_FORMAT_STRING);
        this.htmlDateString = datePrint.format(this.blogPostToDecorate.getDate());
    }

    @Override
    public int getId() {
        return blogPostToDecorate.getId();
    }

    @Override
    public String getTitle() {
        return StringEscapeUtils.unescapeHtml4(blogPostToDecorate.getTitle());
    }

    @Override
    public String getAuthorDate() {
        return StringEscapeUtils.unescapeHtml4(blogPostToDecorate.getAuthor() + "\n" + this.htmlDateString);
    }

    @Override
    public String getContent() {
        String unescapedContent = StringEscapeUtils.unescapeHtml4(blogPostToDecorate.getContent());
        return "<h3>" + blogPostToDecorate.getTitle() + "</h3><h4>" + blogPostToDecorate.getAuthor() + "</h4><h5>" + this.htmlDateString + "</h5>" + unescapedContent;
    }

    @Override
    public String getAuthor() {
        return StringEscapeUtils.unescapeHtml4(blogPostToDecorate.getAuthor() + "\n" + this.htmlDateString);
    }

    @Override
    public Date getDate() {
        return blogPostToDecorate.getDate();
    }

    public String getHtmlDateString() {
        return this.htmlDateString;
    }
}
