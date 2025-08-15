package com.aimprosoft.glossary.test;

import com.aimprosoft.glossary.common.model.impl.User;
import org.junit.Test;

import javax.persistence.Column;

import static org.junit.Assert.*;

public class UserModelTest {
    @Test
    public void passwordColumnShouldNotBeUnique() throws NoSuchFieldException {
        Column column = User.class.getDeclaredField("password").getAnnotation(Column.class);
        assertNotNull("password field should have Column annotation", column);
        assertFalse("password column should not be unique", column.unique());
    }
}
