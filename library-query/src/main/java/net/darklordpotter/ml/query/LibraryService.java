package net.darklordpotter.ml.query;

import com.hubspot.dropwizard.guice.GuiceBundle;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.auth.CachingAuthenticator;
import com.yammer.dropwizard.auth.basic.BasicAuthProvider;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.jdbi.DBIFactory;
import net.darklordpotter.ml.core.Story;
import net.darklordpotter.ml.query.api.User;
import net.darklordpotter.ml.query.core.DlpAuthenticator;
import net.darklordpotter.ml.query.core.MongoClientManager;
import net.darklordpotter.ml.query.healthcheck.MongoHealthCheck;
import net.darklordpotter.ml.query.jdbi.*;
import net.darklordpotter.ml.query.resources.*;
import net.vz.mongodb.jackson.JacksonDBCollection;
import org.skife.jdbi.v2.DBI;

import java.util.concurrent.TimeUnit;

/**
 * 2013-02-09
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
public class LibraryService extends Service<LibraryConfiguration> {

    @Override
    public void initialize(Bootstrap<LibraryConfiguration> bootstrap) {
//        bootstrap.addBundle(
//                GuiceBundle.newBuilder()
//                .addModule(new DlpModule())
//                        .enableAutoConfig()
//                .build()
//        );
    }

    @Override
    public void run(LibraryConfiguration configuration, Environment environment) throws Exception {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDatabaseConfiguration(), "mysql");

        final PostDAO dao = jdbi.onDemand(PostDAO.class);
        final AuthDAO authDAO = jdbi.onDemand(AuthDAO.class);
        final ForumDAO forumDAO = jdbi.onDemand(ForumDAO.class);
        final ThreadDAO threadDAO = jdbi.onDemand(ThreadDAO.class);
        final SubscriptionDAO subscriptionDAO = jdbi.onDemand(SubscriptionDAO.class);
        final SimilarityDAO similarityDAO = jdbi.onDemand(SimilarityDAO.class);

        final MongoClientManager mongoClientManager = new MongoClientManager(configuration.mongoHost, configuration.mongoPort);
        final MongoClient client = mongoClientManager.getClient();
        final DB db = client.getDB(configuration.mongoDatabaseName);

        final DBCollection collection = db.getCollection("stories");
        final JacksonDBCollection<Story, String> libraryCollection = JacksonDBCollection.wrap(collection, Story.class, String.class);

        environment.addProvider(
                new BasicAuthProvider<>(
                        CachingAuthenticator.wrap(
                                new DlpAuthenticator(authDAO),
                                configuration.getAuthenticationCachePolicy()
                        ),
                        "DLP API"
                )
        );

        environment.addFilter(CORSFilter.class, "/*");
        environment.addFilter(new RateLimitingFilter(5, 5, TimeUnit.SECONDS), "/ffn/*");
        environment.addHealthCheck(new MongoHealthCheck(client));

        // Misc API
        environment.addResource(new MainResource());
        environment.addResource(new WbaResource(dao));
        environment.addResource(new FFNResource());
        environment.addResource(new TagsResource(collection));
        environment.addResource(new StoryResource(collection));
        environment.addResource(new SimilarityResource(similarityDAO, libraryCollection));

        // Forums API
        environment.addResource(new PostResource(dao));
        environment.addResource(new AuthResource(authDAO));
        environment.addResource(new ForumResource(forumDAO, threadDAO));
        environment.addResource(new SubscriptionResource(subscriptionDAO));
    }

    public static void main(String[] args) throws Exception {
        new LibraryService().run(args);
    }

}
