package com.coveros.coverosmobileapp;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assume.assumeTrue;

/**
 * Created by maria on 6/20/2017.
 */

public class PostListTest {

    PostList postList = new PostList();

    @Test
    public void addPosts_withWordpressConnection() throws InterruptedException, IOException {
        assumeTrue(isWordpressAvailable());


    }

    public boolean isWordpressAvailable() throws InterruptedException, IOException {
        String command = "ping -c 1 wordpress.org";
        return (Runtime.getRuntime().exec (command).waitFor() == 0);
    }



}
