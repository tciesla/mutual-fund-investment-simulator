package pl.tciesla.mutual.fund.simulator.server.config;

import pl.tciesla.mutual.fund.simulator.server.rest.CustomerResource;
import pl.tciesla.mutual.fund.simulator.server.rest.MutualFundResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Configuration class instead of web.xml file.
 */
@ApplicationPath("/")
public class ApplicationConfig extends Application {

    private static final Set<Class<?>> resources = new HashSet<>();

    static {
        resources.add(CustomerResource.class);
        resources.add(MutualFundResource.class);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return resources;
    }

}
