package net.darklordpotter.ml.query.resources;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.mongodb.*;
import groovy.lang.Closure;
import net.darklordpotter.ml.core.Story;
import net.darklordpotter.ml.query.api.TagResult;
import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.JacksonDBCollection;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
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
    private final DBCollection libraryCollection;
    private final JacksonDBCollection<Story, String> jacksonDBCollection;
    private final Logger log = LoggerFactory.getLogger(TagsResource.class);
    private final Ordering<TagResult> tagResultOrdering = new Ordering<TagResult>() {
        @Override
        public int compare(TagResult left, TagResult right) {
            return left.getCount().compareTo(right.getCount());
        }
    };

    public TagsResource(final DBCollection libraryCollection) {
        this.libraryCollection = libraryCollection;
        this.jacksonDBCollection = JacksonDBCollection.wrap(libraryCollection, Story.class, String.class);
    }

    @GET
    public Iterable<TagResult> getAllTags(@QueryParam("startsWith") String startsWith) {
        return getTags();
    }

    protected List<TagResult> getTags() {
        Iterable<DBObject> objects = tagAggregation();

        log.info(objects.toString());

        Iterable<TagResult> unsortedTags = Iterables.transform(objects, new Function<DBObject, TagResult>() {
            @Override
            public TagResult apply(DBObject input) {
                if (input != null)
                    return new TagResult((String)input.get("_id"), (long)(int)input.get("count"));
                else return null;
            }
        });

        log.info(unsortedTags.toString());



        return tagResultOrdering.reverse().sortedCopy(unsortedTags);
    }

    // db.stories.aggregate(   { $project : {      author : 1,      tags : 1,   } },   { $unwind : "$tags" },   { $group : {      _id : "$tags",      count : { $sum : 1 }   } } );
    protected Iterable<DBObject> tagAggregation() {
        return libraryCollection.aggregate(
                new BasicDBObject("$project", new BasicDBObject("tags", 1)),
                new BasicDBObject("$unwind", "$tags"),
                new BasicDBObject("$group",
                        BasicDBObjectBuilder.start("_id", "$tags").add("count", new BasicDBObject("$sum", 1)).get())
                ).results();
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
}
