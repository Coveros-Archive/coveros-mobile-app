package com.coveros.coverosmobileapp.blogpost;

import com.google.gson.JsonObject;

import org.apache.commons.text.StringEscapeUtils;

/**
 * Created by brian on 7/14/17.
 */

public class TitleBlogPostParser implements BlogPostJsonParser {
    @Override
    public String parseBlogObject(JsonObject blogPostJsonObject) {
        return StringEscapeUtils.unescapeHtml4(blogPostJsonObject.get("title").getAsJsonObject().get("rendered").getAsString());
    }
}
