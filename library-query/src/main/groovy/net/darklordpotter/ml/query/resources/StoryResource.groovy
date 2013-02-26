package net.darklordpotter.ml.query.resources

import com.google.common.base.Splitter
import com.mongodb.*
import com.yammer.metrics.annotation.Metered
import net.darklordpotter.ml.core.Story
import net.vz.mongodb.jackson.DBQuery
import net.vz.mongodb.jackson.JacksonDBCollection

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import java.util.regex.Pattern

/**
 * 2013-02-09
 * @author Michael Rose <elementation@gmail.com>
 */
@Path("/stories")
@Produces("application/json; charset=utf-8")
class StoryResource {
    private final DBCollection libraryCollection
    private final JacksonDBCollection<Story, String> jacksonDBCollection

    StoryResource(final DBCollection libraryCollection) {
        this.libraryCollection = libraryCollection
        this.jacksonDBCollection = JacksonDBCollection.wrap(libraryCollection, Story, String)
    }

    @GET
    @Metered
    Iterator<Story> getAllStories(
        @QueryParam("ratingThreshold") Double threshold,
        @QueryParam("sortField") String sortField,
        @QueryParam("sortDirection") String sortDirection,
        @QueryParam("title") String title
    ) {
        DBObject thresholdQuery
        if (threshold) {
            thresholdQuery = DBQuery.and(
                    DBQuery.regex("title", Pattern.compile("${title}")),
                    DBQuery.greaterThanEquals("threadRating", threshold)
            )
        } else {
            thresholdQuery = DBQuery.regex("title", Pattern.compile("${title ?: ''}"))
        }

        //constructThresholdQuery(threshold)

//        libraryCollection.

        jacksonDBCollection.find(thresholdQuery).sort(
                    new BasicDBObject(sortField ?: 'title', translateSortToInt(sortDirection))
                )
    }

    @GET
    @Path("/{storyId}")
    Story getStory(@PathParam("storyId") Long storyId) {
        Story story = jacksonDBCollection.findOneById(storyId.toString())

        if (!story) throw new WebApplicationException(Response.Status.NOT_FOUND)

        story
    }

    @GET
    @Path("/tagged/{tag}")
    Iterator<Story> getTaggedStories(
            @PathParam("tag") String tags,
            @QueryParam("ratingThreshold") Double threshold,
            @QueryParam("sortField") String sortField,
            @QueryParam("sortDirection") String sortDirection) {
        Collection<String> tagList = Splitter.on(",").omitEmptyStrings().trimResults().split(tags).collect()
        println tagList
        jacksonDBCollection.find(DBQuery.all("tags", tagList)).sort(
                new BasicDBObject(sortField ?: 'title', translateSortToInt(sortDirection))
        )
    }

    protected int translateSortToInt(String sortDirection) {
        sortDirection == "DESC" ? -1 : 1
    }
}
