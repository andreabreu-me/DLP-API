package net.darklordpotter.ml.query

import com.mongodb.DB
import com.mongodb.DBCollection
import com.mongodb.MongoClient
import com.yammer.dropwizard.Service
import com.yammer.dropwizard.config.Bootstrap
import com.yammer.dropwizard.config.Configuration
import com.yammer.dropwizard.config.Environment
import net.darklordpotter.ml.query.resources.StoryResource
import net.darklordpotter.ml.query.resources.TagsResource

/**
 * 2013-02-09
 * @author Michael Rose <michael@fullcontact.com>
 */
class LibraryService extends Service<Configuration> {
    @Override
    void initialize(Bootstrap<Configuration> bootstrap) {

    }

    @Override
    void run(Configuration configuration, Environment environment) throws Exception {
        final MongoClient client = new MongoClient("localhost")
        final DB db = client.getDB("dlp_library")
        final DBCollection collection = db.getCollection("stories")

        environment.addFilter(CORSFilter, "/*")
        environment.addResource(new StoryResource(collection))
        environment.addResource(new TagsResource(collection))
    }

    public static void main(String[] args) {
        new LibraryService().run(args)
    }
}
