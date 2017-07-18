package com.coveros.coverosmobileapp.blogpost;

import java.util.Date;

/**
 * Common interface for Blog Posts and related objects
 *
 * Created by brian on 7/17/17.
 */

public interface BlogPostItem {
    /**
     * The unique ID of the blog post
     *
     * @return
     */
    int getId();

    /**
     * The title of the blog post
     *
     * @return
     */
    String getTitle();

    /**
     * Combination of author and date used for display
     *
     * @return
     */
    String getAuthorDate();

    /**
     * The text of the blog post
     *
     * @return
     */
    String getContent();

    /**
     * The name of the author of the blog post
     *
     * @return
     */
    String getAuthor ();

    /**
     * The date on which the blog post was published
     *
     * @return
     */
    Date getDate();
}
