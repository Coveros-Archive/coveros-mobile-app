package com.coveros.coverosmobileapp.blogpost;

/**
 *
 *
 * Created by brian on 7/13/17.
 *
 */
public class InvalidBlogPostJsonDateException extends RuntimeException {

    public InvalidBlogPostJsonDateException(Exception cause) {
        super(cause);
    }

    public InvalidBlogPostJsonDateException() {

    }
}
