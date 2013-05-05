package net.darklordpotter.ml.query.resources;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import groovy.lang.Closure;
import net.darklordpotter.ml.core.Story;
import net.darklordpotter.ml.query.api.TagResult;
import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.JacksonDBCollection;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * 2013-02-09
 *
 * @author Michael Rose <elementation@gmail.com>
 */
@Path("/tags")
@Produces("application/json; charset=utf-8")
public class TagsResource {
    public TagsResource(final DBCollection libraryCollection) {
        this.libraryCollection = libraryCollection;
        this.jacksonDBCollection = JacksonDBCollection.wrap(libraryCollection, Story.class, String.class);
    }

    @GET
    public Iterable<TagResult> getAllTags(@QueryParam("startsWith") String startsWith) {
        return getTags();
    }

    protected List<TagResult> getTags() {
        // db.stories.aggregate(   { $project : {      author : 1,      tags : 1,   } },   { $unwind : "$tags" },   { $group : {      _id : "$tags",      count : { $sum : 1 }   } } );
        return DefaultGroovyMethods.reverse(DefaultGroovyMethods.sort(DefaultGroovyMethods.collect(libraryCollection.aggregate(new BasicDBObject("$project", new BasicDBObject("tags", 1)), new BasicDBObject("$unwind", "$tags"), new BasicDBObject("$group", BasicDBObjectBuilder.start("_id", "$tags").add("count", new BasicDBObject("$sum", 1)).get())).results(), new Closure<TagResult>(this, this) {
            public TagResult doCall(Object it) {
                return new TagResult(((DBObject) it).get("_id").toString(), Long.parseLong(((DBObject) it).get("count").toString()));
            }

            public TagResult doCall() {
                return doCall(null);
            }

        }), new Closure<Long>(this, this) {
            public Long doCall(Object it) {return ((TagResult) it).getCount();}

            public Long doCall() {
                return doCall(null);
            }

        }));
    }

    protected DBObject constructThresholdQuery(Double threshold) {
        if (DefaultGroovyMethods.asBoolean(threshold)) {
            return DBQuery.greaterThanEquals("threadRating", threshold);
        } else {
            return new BasicDBObject();
        }

    }

    protected int translateSortToInt(String sortDirection) {
        return sortDirection.equals("DESC") ? -1 : 1;
    }

    private final DBCollection libraryCollection;
    private final JacksonDBCollection<Story, String> jacksonDBCollection;
}
