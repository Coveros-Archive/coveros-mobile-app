package com.coveros.coverosmobileapp.post;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.widget.TextView;

import com.coveros.coverosmobileapp.R;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class PostReadActivityTest {

    private static final String HEADING = "The Ugly Barnacle";
    private static final String SUBHEADING = "Marie Kin - July 4 2005";
    private static final String CONTENT = "Once upon a time there was an ugly barnacle. He was so ugly that everyone died. The end.";

    @Rule
    public ActivityTestRule<PostReadActivity> postReadActivityRule = new ActivityTestRule<PostReadActivity>(PostReadActivity.class) {
        @Override
        public Intent getActivityIntent() {
            Intent intent = new Intent();
            ArrayList<String> postData = new ArrayList<>();
            postData.add(HEADING);
            postData.add(SUBHEADING);
            postData.add(CONTENT);
            intent.putStringArrayListExtra("postData", postData);
            return intent;
        }
    };

    @Test
    public void onCreateWithData() {
        assertEquals("The correct heading should be set", HEADING,
                ((TextView) postReadActivityRule.getActivity().findViewById(R.id.heading)).getText());
        assertEquals("The correct subheading should be set", SUBHEADING,
                ((TextView) postReadActivityRule.getActivity().findViewById(R.id.subheading)).getText());
        assertTrue("The content should be displayed",
                postReadActivityRule.getActivity().findViewById(R.id.content).isShown());
    }
}
