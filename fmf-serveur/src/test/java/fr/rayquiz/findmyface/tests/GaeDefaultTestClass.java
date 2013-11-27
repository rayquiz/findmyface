package fr.rayquiz.findmyface.tests;

import org.junit.After;
import org.junit.Before;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;

import static com.googlecode.objectify.ObjectifyService.ofy;

public abstract class GaeDefaultTestClass {

    protected final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig(),
            new LocalMemcacheServiceTestConfig(), new LocalUserServiceTestConfig());

    @Before
    public void setUp() {
        helper.setUp();
        ofy().clear();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

}
