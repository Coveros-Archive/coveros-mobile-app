package com.coveros.coverosmobileapp.blogpost;

import java.util.Date;


/**
 * Represents a blog post.
 *
 * @author Maria Kim
 */
class BlogPost implements BlogPostItem {

    private int id;

    private String content;
    private String title;
    private String authorDate;

    private Date date;

    private String author;

    BlogPost(int id, Date date, String content, String title, String author) {
        this.id = id;
        this.date = date;
        this.content = content;
        this.title = title;
        this.author = author;
        this.authorDate = author + "\n" + this.date;
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorDate() {
        return authorDate;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return this.author;
    }

    public Date getDate() {
        return this.date;
    }

    @Override
    public String toString() {
        return String.format("Heading: %s%nSubheading: %s", title, authorDate);
    }
}

