package net.darklordpotter.ml.query

import com.mongodb.DB
import com.mongodb.DBCollection
import com.mongodb.MongoClient
import com.yammer.dropwizard.Service
import com.yammer.dropwizard.config.Bootstrap
import com.yammer.dropwizard.config.Environment
import net.darklordpotter.ml.query.core.MongoClientManager
import net.darklordpotter.ml.query.healthcheck.MongoHealthCheck
import net.darklordpotter.ml.query.resources.FFNResource
import net.darklordpotter.ml.query.resources.MainResource
import net.darklordpotter.ml.query.resources.StoryResource
import net.darklordpotter.ml.query.resources.TagsResource

import java.util.concurrent.TimeUnit

/**
 * 2013-02-09
 * @author Michael Rose <elementation@gmail.com>
 */
class LibraryService extends Service<LibraryConfiguration> {
    @Override
    void initialize(Bootstrap<LibraryConfiguration> bootstrap) {

    }

    @Override
    void run(LibraryConfiguration configuration, Environment environment) throws Exception {
        MongoClientManager mongoClientManager = new MongoClientManager(configuration.mongoDsn)

        final MongoClient client = mongoClientManager.getClient()

        final DB db = client.getDB(configuration.mongoDatabaseName)
        final DBCollection collection = db.getCollection("stories")

        environment.addFilter(CORSFilter, "/*")
        environment.addFilter(new RateLimitingFilter(5, 5, TimeUnit.SECONDS), "/ffn/*")
        environment.addResource(new MainResource())
        environment.addResource(new StoryResource(collection))
        environment.addResource(new TagsResource(collection))
        environment.addResource(new FFNResource())
        environment.addHealthCheck(new MongoHealthCheck(client))
    }

    public static void main(String[] args) {
        new LibraryService().run(args)
    }
}
