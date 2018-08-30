package com.coveros.coverosmobileapp.blogpost;

import android.support.test.rule.ActivityTestRule;
import android.widget.TextView;

import com.coveros.coverosmobileapp.test.util.LooperTestSuite;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * @author Maria Kim
 */

public class BlogListActivityInstrumentedTest extends LooperTestSuite {

    private static BlogListActivity blogListActivity;

    @Rule
    public ActivityTestRule<BlogListActivity> blogListRule = new ActivityTestRule<>(BlogListActivity.class);

    @Before
    public void setUp() {

        blogListActivity = blogListRule.getActivity();

    }

    @Test
    public void createTextViewLabel_forComments() {
        final String EXPECTED_TEXT_VIEW_LABEL = "Comments";
        final float EXPECTED_TEXT_SIZE = blogListActivity.getTextViewTextSize();
        final int EXPECTED_PADDING_BOTTOM = blogListActivity.getTextViewPaddingBottom();

        TextView commentsLabel = blogListActivity.createTextViewLabel(blogListActivity, "Comments");

        String actualTextViewLabel = (String) commentsLabel.getText();
        assertThat(actualTextViewLabel, equalTo(EXPECTED_TEXT_VIEW_LABEL));

        float actualTextSize = commentsLabel.getTextSize();
        assertThat(actualTextSize, equalTo(EXPECTED_TEXT_SIZE));

        int actualPaddingBottom = commentsLabel.getPaddingBottom();
        assertThat(actualPaddingBottom, equalTo(EXPECTED_PADDING_BOTTOM));
    }





}
