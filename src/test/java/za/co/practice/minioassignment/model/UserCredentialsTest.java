package za.co.practice.minioassignment.model;

import org.junit.Assert;
import org.junit.Test;

public class UserCredentialsTest {

    @Test
    public void testConstructor() {
        UserCredentials toTest = new UserCredentials("", "");
        Assert.assertNotNull(toTest);
    }

    @Test
    public void testFields() {
        UserCredentials toTest = new UserCredentials("", "");
        Assert.assertNotNull(toTest.getUsername());
        Assert.assertNotNull(toTest.getPassword());
    }
}
