package net.darklordpotter.ml.query.core

import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.MongoClientURI
import com.yammer.dropwizard.lifecycle.Managed

/**
 * 2013-02-11
 * @author Michael Rose <elementation@gmail.com>
 */
class MongoClientManager implements Managed {
    private final MongoClient client;

    MongoClientManager(String mongoUri) {
        client = new MongoClient(new MongoClientURI(mongoUri,
                    new MongoClientOptions.Builder()
                            .connectionsPerHost(250)
                            .socketTimeout(5000)
                            .connectTimeout(10)
                            .maxWaitTime(5000)
                            .threadsAllowedToBlockForConnectionMultiplier(5)
                    )
                )
    }

    void start() throws Exception {

    }

    void stop() throws Exception {
        client.close()
    }

    MongoClient getClient() {
        return client
    }
}
