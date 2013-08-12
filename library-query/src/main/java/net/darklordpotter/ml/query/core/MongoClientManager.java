package net.darklordpotter.ml.query.core;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.yammer.dropwizard.lifecycle.Managed;

import java.net.UnknownHostException;

/**
 * 2013-02-11
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
public class MongoClientManager implements Managed {
    private final MongoClient client;

    public MongoClientManager(String mongoUri, Integer mongoPort) throws UnknownHostException {
        client = new MongoClient(new ServerAddress(mongoUri, mongoPort),
                new MongoClientOptions.Builder()
                        .connectionsPerHost(250)
                        .socketTimeout(5000)
                        .connectTimeout(10)
                        .maxWaitTime(5000)
                        .threadsAllowedToBlockForConnectionMultiplier(5).build());
    }

    public void start() throws Exception {

    }

    public void stop() throws Exception {
        client.close();
    }

    public MongoClient getClient() {
        return client;
    }
}
