package com.coveros.coverosmobileapp.blogpost;

import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * @author Maria Kim
 */

public class CommentFormActivityTest {

    @Test
    public void checkFieldIsEmpty_withThreeEmptyStrings() {
        String expectedAuthorName = "name";
        String expectedEmailString = "email";
        String expectedMessageString = "message";

        List<String> actualEmptyFields = CommentFormActivity.checkFieldIsEmpty("", "", "");
        String actualAuthorString = actualEmptyFields.get(0);
        String actualEmailString = actualEmptyFields.get(1);
        String actualMessageString = actualEmptyFields.get(2);
        assertEquals("\"Author\" strings should match", expectedAuthorName, actualAuthorString);
        assertEquals("\"Email\" strings should match", expectedEmailString, actualEmailString);
        assertEquals("\"Message\" strings should match", expectedMessageString, actualMessageString);

    }

}
