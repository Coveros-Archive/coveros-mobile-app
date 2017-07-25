package com.coveros.coverosmobileapp.blogpost;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.util.SparseArray;

import com.coveros.coverosmobileapp.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class BlogPostReadActivityInstrumentedTest {

    private static BlogPostReadActivity blogPostReadActivity;

    private static final String EXPECTED_TITLE = "Scripting with OWASP ZAP";

    @Rule
    public ActivityTestRule<BlogPostReadActivity> blogPostReadActivityRule = new ActivityTestRule<BlogPostReadActivity>(BlogPostReadActivity.class) {
        @Override
        public Intent getActivityIntent() {
            Intent intent = new Intent();
            intent.putExtra("blogId", 6674);
            SparseArray<String> authors = new SparseArray<>();
            authors.append(42, "Ryan Kenny");
            return intent;
        }
    };

    @Before
    public void setUp() {
        blogPostReadActivity = blogPostReadActivityRule.getActivity();
    }

    @Test
    public void onCreate_checkWebViewContentIsShown() {
        boolean webViewContentIsShown = blogPostReadActivity.findViewById(R.id.content).isShown();
        assertThat(webViewContentIsShown, is(true));
    }

    @Test
    public void onCreate_checkViewCommentsButtonIsShown() {
        boolean viewCommentsButtonIsShown = blogPostReadActivity.findViewById(R.id.view_comments).isShown();
        assertThat(viewCommentsButtonIsShown, is(true));
    }


}
