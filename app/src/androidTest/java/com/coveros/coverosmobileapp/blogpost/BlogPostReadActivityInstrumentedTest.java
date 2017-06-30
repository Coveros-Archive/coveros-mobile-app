package com.coveros.coverosmobileapp.blogpost;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import com.coveros.coverosmobileapp.R;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BlogPostReadActivityInstrumentedTest {

    private static final String TITLE = "The Ugly Barnacle";
    private static final String CONTENT = "<h3>The Ugly Barnacle</h3><h4>Marie Kin</h4><h5>Jul 4 2005O</h5>Once upon a time there was an ugly barnacle. He was so ugly that everyone died. The end.";
    private static final String ID = "0";

    @Rule
    public ActivityTestRule<BlogPostReadActivity> postReadActivityRule = new ActivityTestRule<BlogPostReadActivity>(BlogPostReadActivity.class) {
        @Override
        public Intent getActivityIntent() {
            Intent intent = new Intent();
            ArrayList<String> postData = new ArrayList<>();
            postData.add(TITLE);
            postData.add(CONTENT);
            postData.add(ID);
            intent.putStringArrayListExtra("postData", postData);
            return intent;
        }
    };

    @Test
    public void onCreate_checkTitlesMatch() {
        assertEquals("Titles should match", TITLE, postReadActivityRule.getActivity().getTitle());
    }

    @Test
    public void onCreate_checkWebViewContentIsShown() {
        assertTrue("Content should be displayed", postReadActivityRule.getActivity().findViewById(R.id.content).isShown());
    }

    @Test
    public void onCreate_checkViewCommentsButtonIsShown() {
        assertTrue("Content should be displayed", postReadActivityRule.getActivity().findViewById(R.id.view_comments).isShown());
    }


}
