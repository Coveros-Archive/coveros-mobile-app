package com.coveros.coverosmobileapp.blogpost;

import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * @author Maria Kim
 */

public class CommentFormActivityTest {

    private static final String EXPECTED_AUTHOR_STRING = "name";
    private static final String EXPECTED_EMAIL_STRING = "email";
    private static final String EXPECTED_MESSAGE_STRING = "message";

    @Test
    public void checkFieldIsEmpty_withThreeEmptyStrings() {
        List<String> actualEmptyFields = CommentFormActivity.checkFieldIsEmpty("", "", "");
        String actualAuthorString = actualEmptyFields.get(0);
        String actualEmailString = actualEmptyFields.get(1);
        String actualMessageString = actualEmptyFields.get(2);
        assertEquals("\"Author\" strings should match", EXPECTED_AUTHOR_STRING, actualAuthorString);
        assertEquals("\"Email\" strings should match", EXPECTED_EMAIL_STRING, actualEmailString);
        assertEquals("\"Message\" strings should match", EXPECTED_MESSAGE_STRING, actualMessageString);

    }

}
