package org.pac4j.jax.rs;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.test.TestPortProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.pac4j.core.config.Config;
import org.pac4j.jax.rs.features.Pac4JSecurityFeature;

import io.undertow.server.session.SessionCookieConfig;

public class RestEasyUndertowServletTest extends AbstractTest {

    private UndertowJaxrsServer server;

    public class MyApp extends Application {
        @Override
        public Set<Class<?>> getClasses() {
            Set<Class<?>> classes = new HashSet<>();
            classes.add(TestResource.class);
            return classes;
        }

        @Override
        public Set<Object> getSingletons() {
            Config config = getConfig();
            Set<Object> singletons = new HashSet<>();
            singletons.add(new Pac4JSecurityFeature(config));
            return singletons;
        }
    }

    @Before
    public void setUp() {
        // TODO use an autogenerated port...
        System.setProperty("org.jboss.resteasy.port", "24257");
        server = new UndertowJaxrsServer().start();

        server.deploy(new MyApp());
    }

    @After
    public void tearDown() {
        server.stop();
    }

    @Override
    protected WebTarget getTarget(String url) {
        return new ResteasyClientBuilder().build().target(TestPortProvider.generateURL(url));
    }

    @Override
    protected String cookieName() {
        return SessionCookieConfig.DEFAULT_SESSION_ID;
    }

    // TODO we don't have injection yet for something else than Jersey!
    @Ignore
    @Override
    public void testInject() {
        // do nothing
    }
}