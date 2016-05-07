package pl.tciesla.simulator.server.conf;

import pl.tciesla.simulator.server.service.CustomerServiceBean;
import pl.tciesla.simulator.server.service.FundsServiceBean;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Configuration class instead of web.xml file.
 */
@ApplicationPath("/")
public class ApplicationConfig extends Application {

    private final Set<Class<?>> resources;

    public ApplicationConfig() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(CustomerServiceBean.class);
        classes.add(FundsServiceBean.class);
        resources = Collections.unmodifiableSet(classes);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return resources;
    }
}
