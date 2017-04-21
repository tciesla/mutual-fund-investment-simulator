package pl.tciesla.simulator.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * Stores client instance for performance purpose.
 */
public class ClientSingleton {

    private static final Client client = Client.create(new DefaultClientConfig());

    /**
     * Returns REST Client instance.
     * @return client instance.
     */
    public static Client getClient() {
        return client;
    }
}
