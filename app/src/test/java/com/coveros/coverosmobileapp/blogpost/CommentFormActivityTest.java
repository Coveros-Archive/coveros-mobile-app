package com.coveros.coverosmobileapp.blogpost;

import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * @author Maria Kim
 */

public class CommentFormActivityTest {

    private static final int EXPECTED_EMPTY_FIELDS_LIST_SIZE = 3;

    @Test
    public void checkFieldIsEmpty_withThreeEmptyStrings() {
        List<String> actualEmptyFields = CommentFormActivity.checkFieldIsEmpty("", "", "");
        int actualEmptyFieldsListSize = actualEmptyFields.size();
        assertEquals("Number of empty fields should equal 3", EXPECTED_EMPTY_FIELDS_LIST_SIZE, actualEmptyFieldsListSize);
    }
}
