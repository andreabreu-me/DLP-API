package net.darklordpotter.ml.query

import com.yammer.dropwizard.Service
import com.yammer.dropwizard.config.Bootstrap
import com.yammer.dropwizard.config.Configuration
import com.yammer.dropwizard.config.Environment
import net.darklordpotter.ml.query.resources.StoryResource

/**
 * 2013-02-09
 * @author Michael Rose <michael@fullcontact.com>
 */
class LibraryService extends Service<Configuration> {
    @Override
    void initialize(Bootstrap bootstrap) {

    }

    @Override
    void run(Configuration configuration, Environment environment) throws Exception {
        environment.addResource(StoryResource)
    }

    public static void main(String[] args) {
        new LibraryService().run(args)
    }
}
