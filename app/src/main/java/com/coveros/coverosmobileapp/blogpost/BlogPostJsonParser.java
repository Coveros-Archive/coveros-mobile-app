package com.coveros.coverosmobileapp.blogpost;

import com.google.gson.JsonObject;

/**
 * Created by brian on 7/14/17.
 */

public interface BlogPostJsonParser {

    String parseBlogObject(JsonObject blogPostJsonObject);

}
