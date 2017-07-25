package com.coveros.coverosmobileapp.website;

import org.junit.Before;
import org.junit.Test;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CustomWebClientTest {

    /*
     * Check on Getter/Setter for Connection
     */
    CustomWebViewClient customWebViewClient;
    @Before
    public void setUp() {
        customWebViewClient = new CustomWebViewClient(new MainActivity());
    }

    @Test
    public void checkIsBlogPost_withBlogPostClassNames() {
        boolean expectedIsBlogPost = true;
        boolean actualIsBlogPost = customWebViewClient.checkIsBlogPost("post-template-default single single-post postid-7573 single-format-standard wp-custom-logo group-blog");

        assertThat(actualIsBlogPost, equalTo(expectedIsBlogPost));

    }
    @Test
    public void checkIsBlogPost_withoutBlogPostClassNames() {
        boolean expectedIsBlogPost = false;
        boolean actualIsBlogPost = customWebViewClient.checkIsBlogPost("post-template-default single single-post single-format-standard wp-custom-logo group-blog");

        assertThat(actualIsBlogPost, equalTo(expectedIsBlogPost));
    }

    @Test
    public void checkIsBlog_withBlogUrl() {
        boolean expectedIsBlog = true;

        boolean actualIsBlog = customWebViewClient.checkIsBlog("coveros.com/blog/ryane-loves-dogs-more-than-cats");
        assertThat(actualIsBlog, equalTo(expectedIsBlog));

        actualIsBlog = customWebViewClient.checkIsBlog("coveros.com/category/blogs/ryane-loves-dogs-more-than-cats");
        assertThat(actualIsBlog, equalTo(expectedIsBlog));

        actualIsBlog = customWebViewClient.checkIsBlog("dev.secureci.com/blog/ryane-loves-dogs-more-than-cats");
        assertThat(actualIsBlog, equalTo(expectedIsBlog));

        actualIsBlog = customWebViewClient.checkIsBlog("dev.secureci.com/category/blogs/ryane-loves-dogs-more-than-cats");
        assertThat(actualIsBlog, equalTo(expectedIsBlog));

    }

    @Test
    public void checkIsCoveros_withCoverosUrl() {
        boolean expectedIsCoveros = true;

        boolean actualIsCoveros = customWebViewClient.checkIsCoveros("coveros.com/ryane-hates-cats");
        assertThat(actualIsCoveros, equalTo(expectedIsCoveros));

        actualIsCoveros = customWebViewClient.checkIsCoveros("dev.secureci.com/ryane-hates-cats");
        assertThat(actualIsCoveros, equalTo(expectedIsCoveros));

    }

    @Test
    public void parsePostId() {
        String expectedPostId = "7573";

        String actualPostId = customWebViewClient.parsePostId("post-template-default single single-post postid-7573 single-format-standard wp-custom-logo group-blog");
        assertThat(expectedPostId, equalTo(actualPostId));

        actualPostId = customWebViewClient.parsePostId("postid-7573 post-template-default single single-post single-format-standard wp-custom-logo group-blog");
        assertThat(expectedPostId, equalTo(actualPostId));
    }

}
