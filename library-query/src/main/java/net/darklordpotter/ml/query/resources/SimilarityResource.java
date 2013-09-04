package net.darklordpotter.ml.query.resources;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mongodb.QueryBuilder;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import lombok.Data;
import net.darklordpotter.ml.core.Story;
import net.darklordpotter.ml.query.api.DiscussionThread;
import net.darklordpotter.ml.query.api.Forum;
import net.darklordpotter.ml.query.api.User;
import net.darklordpotter.ml.query.jdbi.ForumDAO;
import net.darklordpotter.ml.query.jdbi.SimilarityDAO;
import net.darklordpotter.ml.query.jdbi.ThreadDAO;
import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.JacksonDBCollection;

import javax.annotation.Nullable;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 2013-06-15
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
@Path("/v1/similar")
@Produces("application/json; charset=utf-8")
public class SimilarityResource {
    private final SimilarityDAO similarityDAO;
    private final JacksonDBCollection<Story, String> jacksonDBCollection;

    public SimilarityResource(SimilarityDAO similarityDAO, JacksonDBCollection<Story, String> jacksonDBCollection) {
        this.similarityDAO = similarityDAO;
        this.jacksonDBCollection = jacksonDBCollection;
    }

    @GET
    @Path("/{threadId}")
    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.HOURS)
    public SimilarityResponse similarStories(@PathParam("threadId") Long threadId) {
        List<Long> similarThreads = similarityDAO.similarThreads(threadId);
        List<String> stringSimilarThreads = Lists.transform(similarThreads, new Function<Long, String>() {
            @Override
            public String apply(Long input) {
                return Long.toString(input);
            }
        });

        System.out.println(stringSimilarThreads);

        Story original = jacksonDBCollection.findOneById(threadId.toString());
        Iterable<Story> storyIterable = jacksonDBCollection.find(DBQuery.in("_id", stringSimilarThreads)).toArray();

        return new SimilarityResponse(original, storyIterable);
    }

    public class SimilarityResponse {
        Story story;
        Iterable<Story> similarStories;

        public SimilarityResponse() {
        }

        public SimilarityResponse(Story story, Iterable<Story> similarStories) {
            this.story = story;
            this.similarStories = similarStories;
        }


        public Story getStory() {
            return story;
        }

        public void setStory(Story story) {
            this.story = story;
        }

        public Iterable<Story> getSimilarStories() {
            return similarStories;
        }

        public void setSimilarStories(Iterable<Story> similarStories) {
            this.similarStories = similarStories;
        }
    }
}
