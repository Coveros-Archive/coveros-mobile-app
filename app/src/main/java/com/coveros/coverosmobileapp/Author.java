package com.coveros.coverosmobileapp;

/**
 * Represents an author.
 * @author Maria Kim
 */
public class Author {

    String name;
    int id;

    public Author(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
