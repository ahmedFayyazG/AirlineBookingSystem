package test.managers;

import managers.UserManager;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserManagerTest {

    @Test
    public void testSingletonInstance() {
        UserManager m1 = UserManager.getInstance();
        UserManager m2 = UserManager.getInstance();
        assertSame(m1, m2); // They should be the same instance
    }
}
