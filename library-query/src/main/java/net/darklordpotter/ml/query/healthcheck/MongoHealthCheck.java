package net.darklordpotter.ml.query.healthcheck;

import com.mongodb.MongoClient;
import com.mongodb.ReplicaSetStatus;
import com.yammer.metrics.core.HealthCheck;

/**
 * 2013-03-23
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
public class MongoHealthCheck extends HealthCheck {
    private final MongoClient client;


    public MongoHealthCheck(MongoClient client) {
        super("mongodb");

        this.client = client;
    }

    @Override
    protected Result check() throws Exception {
        return Result.healthy(client.getConnector().debugString());
    }
}
