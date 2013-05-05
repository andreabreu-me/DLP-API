package net.darklordpotter.ml.query;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.jdbi.DBIFactory;
import net.darklordpotter.ml.query.core.MongoClientManager;
import net.darklordpotter.ml.query.healthcheck.MongoHealthCheck;
import net.darklordpotter.ml.query.jdbi.PostDAO;
import net.darklordpotter.ml.query.resources.*;
import org.skife.jdbi.v2.DBI;

import java.util.concurrent.TimeUnit;

/**
 * 2013-02-09
 *
 * @author Michael Rose <elementation@gmail.com>
 */
public class LibraryService extends Service<LibraryConfiguration> {
    @Override
    public void initialize(Bootstrap<LibraryConfiguration> bootstrap) {

    }

    @Override
    public void run(LibraryConfiguration configuration, Environment environment) throws Exception {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDatabaseConfiguration(), "mysql");
        final PostDAO dao = jdbi.onDemand(PostDAO.class);

        final MongoClientManager mongoClientManager = new MongoClientManager(configuration.mongoHost, configuration.mongoPort);

        final MongoClient client = mongoClientManager.getClient();

        final DB db = client.getDB(configuration.mongoDatabaseName);
        final DBCollection collection = db.getCollection("stories");


        environment.addFilter(CORSFilter.class, "/*");
        environment.addFilter(new RateLimitingFilter(5, 5, TimeUnit.SECONDS), "/ffn/*");
        environment.addResource(new MainResource());
        environment.addResource(new StoryResource(collection));
        environment.addResource(new WbaResource(dao));
        environment.addResource(new TagsResource(collection));
        environment.addResource(new FFNResource());
        environment.addHealthCheck(new MongoHealthCheck(client));
    }

    public static void main(String[] args) throws Exception {
        new LibraryService().run(args);
    }

}
