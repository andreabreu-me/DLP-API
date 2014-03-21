package net.darklordpotter.ml.query.core;

import com.yammer.dropwizard.lifecycle.Managed;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;

import java.util.List;

/**
 * 2013-12-23
 *
 * @author Michael Rose
 */
public class ElasticSearchClientManager implements Managed {
    private Client client = null;
    private final String clusterName;
    private final List<String> hostnames;
    private final int port;
    TransportAddress[] transportAddresses;

    public ElasticSearchClientManager(String clusterName, List<String> hostnames, int port) {
        this.clusterName = clusterName;
        this.hostnames = hostnames;
        this.port = port;

        transportAddresses = new TransportAddress[hostnames.size()];
        for (int i = 0; i < hostnames.size(); i++) {
            transportAddresses[i] = new InetSocketTransportAddress(hostnames.get(i), port);
        }

        client = new TransportClient().addTransportAddresses(transportAddresses);
    }

    @Override
    public void start() throws Exception {
    }

    @Override
    public void stop() throws Exception {
        client.close();
    }

    public Client getClient() {
        return client;
    }
}
