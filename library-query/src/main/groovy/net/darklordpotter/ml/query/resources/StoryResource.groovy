package net.darklordpotter.ml.query.resources

import com.mongodb.BasicDBObject
import com.mongodb.DB
import com.mongodb.DBCollection
import com.mongodb.MongoClient
import net.darklordpotter.ml.core.Story
import net.vz.mongodb.jackson.DBQuery
import net.vz.mongodb.jackson.JacksonDBCollection

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * 2013-02-09
 * @author Michael Rose <michael@fullcontact.com>
 */
@Path("/story")
@Produces(MediaType.APPLICATION_JSON)
class StoryResource {
    static MongoClient client = new MongoClient("localhost")
    static DB db = client.getDB("dlp_library")
    static DBCollection collection = db.getCollection("stories")
    static JacksonDBCollection<Story, String> jacksonDBCollection = JacksonDBCollection.wrap(collection, Story, String)

    @GET
    Iterator<Story> getAllStories() {
        jacksonDBCollection.find(
                DBQuery.greaterThanEquals("threadRating", 4.0d)).sort(
                    new BasicDBObject( "_id" , -1 )
                )
    }
}
