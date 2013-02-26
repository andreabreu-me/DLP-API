package net.darklordpotter.ml.extraction.sinks

import com.mongodb.MongoClient
import net.darklordpotter.ml.core.Story
import net.darklordpotter.ml.extraction.DataSink
import net.vz.mongodb.jackson.JacksonDBCollection

/**
 * 2013-02-25
 * @author Michael Rose <elementation@gmail.com>
 */
class MongoDBSink implements DataSink {
    JacksonDBCollection<Story, String> jacksonDBCollection

    public MongoDBSink(String db, String collection) {
        this("localhost", 27017, db, collection)
    }

    public MongoDBSink(String host, Integer port, String db, String collection) {
        MongoClient client = new MongoClient(host, port)
        jacksonDBCollection = JacksonDBCollection.wrap(client.getDB(db).getCollection(collection), Story, String)
    }

    void insertStory(Story result) {
        jacksonDBCollection.save(result)
    }
}
