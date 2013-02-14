package net.darklordpotter.ml.query.resources

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.mongodb.*
import net.darklordpotter.ml.core.Story
import net.darklordpotter.ml.query.api.TagResult
import net.vz.mongodb.jackson.DBQuery
import net.vz.mongodb.jackson.JacksonDBCollection

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import java.util.concurrent.TimeUnit

/**
 * 2013-02-09
 * @author Michael Rose <michael@fullcontact.com>
 */
@Path("/tags")
@Produces("application/json; charset=utf-8")
class TagsResource {
    private final DBCollection libraryCollection
    private final JacksonDBCollection<Story, String> jacksonDBCollection

    TagsResource(final DBCollection libraryCollection) {
        this.libraryCollection = libraryCollection
        this.jacksonDBCollection = JacksonDBCollection.wrap(libraryCollection, Story, String)
    }

    @GET
    Iterable<TagResult> getAllTags(@QueryParam("startsWith") String startsWith) {
        getTags()
    }

    protected List<TagResult> getTags() {
        // db.stories.aggregate(   { $project : {      author : 1,      tags : 1,   } },   { $unwind : "$tags" },   { $group : {      _id : "$tags",      count : { $sum : 1 }   } } );
        libraryCollection.aggregate(
                new BasicDBObject('$project', new BasicDBObject("tags",1)),
                new BasicDBObject('$unwind', '$tags'),
                new BasicDBObject('$group', BasicDBObjectBuilder.start('_id', '$tags').add('count', new BasicDBObject('$sum', 1)).get())
        ).results().collect {
            new TagResult(it.get("_id").toString(), Long.parseLong(it.get("count").toString()))
        }.sort { it.count }.reverse()
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
