package com.coveros.coverosmobileapp;

/**
 * Represents an author.
 * Created by Maria Kim on 6/19/2017.
 */

public class Author {

    String name;
    int id;

    public Author(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() { return name; }
    public int getId() { return id; }

    public String toString() { return name; }
}
