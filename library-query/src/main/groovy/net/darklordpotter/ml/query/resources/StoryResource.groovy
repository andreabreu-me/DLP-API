package net.darklordpotter.ml.query.resources

import com.google.common.base.Splitter
import com.mongodb.*
import net.darklordpotter.ml.core.Story
import net.vz.mongodb.jackson.DBQuery
import net.vz.mongodb.jackson.JacksonDBCollection

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * 2013-02-09
 * @author Michael Rose <michael@fullcontact.com>
 */
@Path("/stories")
@Produces(MediaType.APPLICATION_JSON)
class StoryResource {
    static MongoClient client = new MongoClient("localhost")
    static DB db = client.getDB("dlp_library")
    static DBCollection collection = db.getCollection("stories")
    static JacksonDBCollection<Story, String> jacksonDBCollection = JacksonDBCollection.wrap(collection, Story, String)

    @GET
    Iterator<Story> getAllStories(
        @QueryParam("ratingThreshold") Double threshold,
        @QueryParam("sortField") String sortField,
        @QueryParam("sortDirection") String sortDirection
    ) {

        DBObject thresholdQuery = constructThresholdQuery(threshold)

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
    Iterator<Story> getTaggedStories(@PathParam("tag") String tags) {
        Collection<String> tagList = Splitter.on(",").omitEmptyStrings().trimResults().split(tags).collect()
        println tagList
        jacksonDBCollection.find(DBQuery.all("tags", tagList)).sort(
                new BasicDBObject("title",-1)
        )
    }

    protected DBObject constructThresholdQuery(Double threshold) {
        if (threshold) {
            return DBQuery.greaterThanEquals("threadRating", threshold)
        } else {
            return new BasicDBObject()
        }
    }

    protected int translateSortToInt(String sortDirection) {
        sortDirection == "DESC" ? -1 : 1
    }
}
