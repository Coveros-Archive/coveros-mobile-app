package com.coveros.coverosmobileapp.blogpost;

import com.thoughtworks.xstream.XStream;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Set;

public class Bookmarks {

    static final String BOOKMARKS_FILE = "bookmarks.xml";

    private final Set<Integer> bookmarks = new HashSet<>();

    private static Bookmarks instance;

    public static synchronized Bookmarks getInstance() {
        if (instance == null) {
            instance = new Bookmarks();
        }

        return instance;
    }

    public boolean removeBookmark(Integer id, FileOutputStream fos) {
        boolean retVal = bookmarks.remove(id);
        saveState(fos);
        return retVal;
    }

    public boolean addBookmark(Integer id, FileOutputStream fos) {
        boolean retVal = bookmarks.add(id);
        saveState(fos);
        return retVal;
    }

    public void loadExistingBookmarks(FileInputStream fis) {
        XStream stream = new XStream();
        //Set<> from = stream.fromXML(fis);
        // TODO read file and populate set here
    }

    private void saveState(FileOutputStream fos) {
        XStream stream = new XStream();
        stream.toXML(bookmarks, fos);
    }
}
