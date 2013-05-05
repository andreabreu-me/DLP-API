package net.darklordpotter.ml.query.resources;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.yammer.metrics.annotation.Metered;
import net.darklordpotter.ml.core.Story;
import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.JacksonDBCollection;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * 2013-02-09
 *
 * @author Michael Rose <elementation@gmail.com>
 */
@Path("/stories")
@Produces("application/json; charset=utf-8")
public class StoryResource {
    private final DBCollection libraryCollection;
    private final JacksonDBCollection<Story, String> jacksonDBCollection;
    private final Logger log = LoggerFactory.getLogger(StoryResource.class);

    public StoryResource(final DBCollection libraryCollection) {
        this.libraryCollection = libraryCollection;
        this.jacksonDBCollection = JacksonDBCollection.wrap(libraryCollection, Story.class, String.class);
    }

    @GET
    @Metered
    public Iterator<Story> getAllStories(@QueryParam("ratingThreshold") Double threshold,
                                         @DefaultValue("adjustedThreadRating") @QueryParam("sortField") String sortField,
                                         @DefaultValue("DESC") @QueryParam("sortDirection") String sortDirection,
                                         @QueryParam("title") final String title) {
        DBObject thresholdQuery;
        if (threshold != null) {
            thresholdQuery = DBQuery.and(DBQuery.regex("title", Pattern.compile(title)),
                    DBQuery.greaterThanEquals("threadRating", threshold));
        } else {
            thresholdQuery = DBQuery.regex("title", Pattern.compile(title != null ? title : ""));
        }


        //constructThresholdQuery(threshold)

//        libraryCollection.

        return jacksonDBCollection.find(thresholdQuery).sort(
                new BasicDBObject(sortField != null ? sortField : "title", translateSortToInt(sortDirection)));
    }

    @GET
    @Metered
    @Path("/{storyId}")
    public Story getStory(@PathParam("storyId") Long storyId) {
        Story story = jacksonDBCollection.findOneById(storyId.toString());

        if (!DefaultGroovyMethods.asBoolean(story))
            throw new WebApplicationException(Response.Status.NOT_FOUND);

        return story;
    }

    @GET
    @Metered
    @Path("/tagged/{tag}")
    public Iterator<Story> getTaggedStories(@PathParam("tag") String tags,
                                            @QueryParam("ratingThreshold") Double threshold,
                                            @QueryParam("sortField") String sortField,
                                            @QueryParam("sortDirection") String sortDirection) {
        Collection<String> tagList = Lists.newArrayList(Splitter.on(",").omitEmptyStrings().trimResults().split(tags));

        log.info("Searching tags with {}", tagList);

        return jacksonDBCollection.find(DBQuery.all("tags", tagList))
                .sort(new BasicDBObject(sortField != null ? sortField : "title", translateSortToInt(sortDirection)));
    }

    protected int translateSortToInt(String sortDirection) {
        return sortDirection != null && sortDirection.equals("DESC") ? -1 : 1;
    }
}
