package pl.tciesla.simulator.client;

import com.sun.jersey.api.client.Client;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientSingletonTest {

    @Test
    public void shouldSingletonHasClientInstance() throws Exception {
        // when
        Client client = ClientSingleton.getClient();
        // then
        assertThat(client).isNotNull();
    }

}