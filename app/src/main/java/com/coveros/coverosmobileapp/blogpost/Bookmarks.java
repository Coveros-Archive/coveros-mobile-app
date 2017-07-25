package com.coveros.coverosmobileapp.blogpost;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.thoughtworks.xstream.XStream;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class Bookmarks {

    private static final String BOOKMARKS_FILE = "bookmarks.xml";

    private static final String TAG = "Bookmarks";

    private static Bookmarks instance;

    private final Set<Integer> bookmarks = new HashSet<>();

    private final XStream stream = new XStream();

    private final String fileName;

    @VisibleForTesting
    Bookmarks(String fileName) {
        this.fileName = fileName;
    }

    static synchronized Bookmarks getInstance() {
        if (instance == null) {
            instance = new Bookmarks(BOOKMARKS_FILE);
        }
        return instance;
    }

    boolean contains(Integer id) {
        return bookmarks.contains(id);
    }

    boolean removeBookmark(Context context, Integer id) {
        boolean retVal = bookmarks.remove(id);
        saveState(context);
        return retVal;
    }

    boolean addBookmark(Context context, Integer id) {
        boolean retVal = bookmarks.add(id);
        saveState(context);
        return retVal;
    }

    boolean loadExistingBookmarks(Context context) {
        if (Arrays.asList(context.fileList()).contains(fileName)) {
            try (FileInputStream fis = context.openFileInput(fileName)) {
                bookmarks.addAll((Set<Integer>) stream.fromXML(fis));
            } catch (IOException ex) {
                // TODO add Toast message
                Log.e(TAG, "Could not load bookmark data from device");
                ex.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }

    private void saveState(Context context) {
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            stream.toXML(bookmarks, fos);
        } catch(IOException ex) {
            // TODO add Toast message
            Log.e(TAG, "Could not save bookmark state to fileName");
        }
    }
}
