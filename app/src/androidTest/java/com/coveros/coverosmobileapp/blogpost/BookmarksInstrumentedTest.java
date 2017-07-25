package com.coveros.coverosmobileapp.blogpost;

import org.junit.After;
import org.junit.Test;
import java.io.File;
import android.content.Context;

import static junit.framework.Assert.assertTrue;

/**
 * Created by SRynestad on 7/25/2017.
 */

public class BookmarksInstrumentedTest {
    private Bookmarks instance;
    private Context context;
    private BlogPost blogPost;

    @After
    public void clean(File fakeBookmarkFile) {
        fakeBookmarkFile.delete();
    }
    @Test
    public void checkIfFileWasLoaded(){
        boolean fileLoaded = instance.loadExistingBookmarks(context);
        assertTrue("existing file should be loaded", fileLoaded);
    }
    @Test
    public void checkIfBookmarkIdWasAdded(){
        boolean bookmarkAdded = instance.addBookmark(context, blogPost.getId());
        assertTrue("bookmark should be added", bookmarkAdded);

    }
    @Test
    public void checkIfBookmarkIdWasRemoved(){
        boolean bookmarkRemoved = instance.removeBookmark(context, blogPost.getId());
        assertTrue("bookmark should be removed", bookmarkRemoved);
    }

}
