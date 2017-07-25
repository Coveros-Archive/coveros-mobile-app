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

/**
 * Uses Xstream to convert XML to Java or Java to XML for storing, removing, or loading bookmarks in XML file
 * @author Sadie Rynestad
 */

public class Bookmarks {

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

    public static synchronized Bookmarks getInstance() {
        if (instance == null) {
            instance = new Bookmarks(BOOKMARKS_FILE);
        }
        return instance;
    }

    /**
     * Checks if the bookmark xml file contains the blogId
     * @param id The blog id being checked
     * @return true if contains the bookmark and false otherwise
     */
    boolean contains(Integer id) {
        return bookmarks.contains(id);
    }

    /**
     *Removes the bookmark id from the xml file
     * @param context Used to open the files
     * @param id The blog id being removed
     * @return true if the bookmark id was removed and false otherwise
     */
    boolean removeBookmark(Context context, Integer id) {
        boolean retVal = bookmarks.remove(id);
        saveState(context);
        return retVal;
    }

    /**
     * Adds a marked bookmark id to the xml file
     * @param context Used to open the file
     * @param id The blog id being added
     * @return true if the bookmark was added and false otherwised
     */
    boolean addBookmark(Context context, Integer id) {
        boolean retVal = bookmarks.add(id);
        saveState(context);
        return retVal;
    }

    /**
     * Loads bookmark ids that have once been added to the xml file
     * @param context Used to open the file
     * @return true if the bookmark ids were loaded to the file and false otherwise
     */
    public boolean loadExistingBookmarks(Context context) {
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

    /**
     * Saves the bookmark state to the xml file
     * @param context Used to open the file
     */
    private void saveState(Context context) {
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            stream.toXML(bookmarks, fos);
        } catch(IOException ex) {
            // TODO add Toast message
            Log.e(TAG, "Could not save bookmark state to fileName");
        }
    }
}
