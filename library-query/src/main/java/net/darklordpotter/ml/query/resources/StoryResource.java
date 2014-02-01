package net.darklordpotter.ml.query.resources;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCollapserKey;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Metered;
import lombok.Data;
import net.darklordpotter.ml.core.Rating;
import net.darklordpotter.ml.core.Story;
import net.darklordpotter.ml.query.Constants;
import net.darklordpotter.ml.query.api.ThreadRating;
import net.darklordpotter.ml.query.api.User;
import net.darklordpotter.ml.query.jdbi.ThreadRatingDao;
import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.JacksonDBCollection;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.funcito.Funcito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.util.functions.Func1;

import static org.funcito.FuncitoGuava.*;

import javax.annotation.Nullable;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 2013-02-09
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
@Api(value = "/stories", description = "Methods around the DLP Library")
@Path("/stories")
@Produces(Constants.APPLICATION_JSON)
public class StoryResource {
    private final DBCollection libraryCollection;
    private final ThreadRatingDao ratingDao;
    private final JacksonDBCollection<Story, String> jacksonDBCollection;
    private final Logger log = LoggerFactory.getLogger(StoryResource.class);
    private final FFDBResource ffdbResource;

    public StoryResource(final DBCollection libraryCollection, final ThreadRatingDao ratingDao, FFDBResource ffdbResource) {
        this.libraryCollection = libraryCollection;
        this.ratingDao = ratingDao;
        this.ffdbResource = ffdbResource;
        this.jacksonDBCollection = JacksonDBCollection.wrap(libraryCollection, Story.class, String.class);
    }

    @ApiOperation("Queries a sorted list of stories")
    @GET
    @Metered
    public Iterator<Story> getAllStories(@QueryParam("ratingThreshold") Double threshold,
                                         @DefaultValue("adjustedThreadRating") @QueryParam("sortField") final String sortField,
                                         @DefaultValue("DESC") @QueryParam("sortDirection") final String sortDirection,
                                         @QueryParam("title") final String title) {
        final DBObject thresholdQuery;
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

    @ApiOperation("Queries a sorted list of stories")
    @GET
    @Metered
    @Path("es")
    public Iterator<Story> getAllStoriesWithEs(@QueryParam("ratingThreshold") Double threshold,
                                         @DefaultValue("adjustedThreadRating") @QueryParam("sortField") final String sortField,
                                         @DefaultValue("DESC") @QueryParam("sortDirection") final String sortDirection,
                                         @QueryParam("title") final String title) {
        final DBObject thresholdQuery;
        if (threshold != null) {
            thresholdQuery = DBQuery.and(DBQuery.regex("title", Pattern.compile(title)),
                    DBQuery.greaterThanEquals("threadRating", threshold));
        } else {
            thresholdQuery = DBQuery.regex("title", Pattern.compile(title != null ? title : ""));
        }


        Observable<Story> storyObservable = Observable.create(new Observable.OnSubscribeFunc<Story>() {
            @Override
            public Subscription onSubscribe(Observer<? super Story> observer) {
                Iterator<Story> storyIterator = jacksonDBCollection.find(thresholdQuery).sort(
                        new BasicDBObject(sortField != null ? sortField : "title", translateSortToInt(sortDirection)));

                while (storyIterator.hasNext()) {
                    Story next = storyIterator.next();
                    observer.onNext(next);
                }

                observer.onCompleted();

                return null;
            }
        });

        Observable<Story> storyObservable1 = storyObservable.map(new Func1<Story, Story>() {
            @Override
            public Story call(Story story) {
//                Optional<Long> id = ffnId(story);
//
//                if (id.isPresent()) {
//                    ffdbResource.getById(id.get());
//                }

                return story;
            }
        }) ;


        //constructThresholdQuery(threshold)

//        libraryCollection.

        return jacksonDBCollection.find(thresholdQuery).sort(
                new BasicDBObject(sortField != null ? sortField : "title", translateSortToInt(sortDirection)));
    }

    @ApiOperation("Queries a sorted list of stories with a user's ratings attached")
    @GET
    @Metered
    @Path("/withRatings")
    public Iterator<StoryAndRating> getStoriesWithRatings(/*@Auth User user, */@QueryParam("ratingThreshold") Double threshold,
                                         @DefaultValue("adjustedThreadRating") @QueryParam("sortField") final String sortField,
                                         @DefaultValue("DESC") @QueryParam("sortDirection") final String sortDirection,
                                         @QueryParam("title") final String title) {
        final DBObject thresholdQuery;
        if (threshold != null) {
            thresholdQuery = DBQuery.and(DBQuery.regex("title", Pattern.compile(title)),
                    DBQuery.greaterThanEquals("threadRating", threshold));
        } else {
            thresholdQuery = DBQuery.regex("title", Pattern.compile(title != null ? title : ""));
        }

        Observable<Story> storyObservable = Observable.create(new Observable.OnSubscribeFunc<Story>() {
            @Override
            public Subscription onSubscribe(Observer<? super Story> observer) {
                Iterator<Story> storyIterator = jacksonDBCollection.find(thresholdQuery).sort(
                        new BasicDBObject(sortField != null ? sortField : "title", translateSortToInt(sortDirection)));

                while (storyIterator.hasNext()) {
                    Story next = storyIterator.next();
                    observer.onNext(next);
                }

                observer.onCompleted();

                return null;
            }
        });

        Observable<StoryAndRating> storyAndRatingObservable = storyObservable.map(new Func1<Story, StoryAndRating>() {
            @Override
            public StoryAndRating call(Story story) {
                return new StoryAndRating(story, ratingDao.getRatingsForUserAndThread(5l, story.getThreadIdLong()));
            }
        }).filter(new Func1<StoryAndRating, Boolean>() {
            @Override
            public Boolean call(StoryAndRating storyAndRating) {
                return storyAndRating.getRating() != null;
            }
        });

//        Observable<ThreadRating> ratingObservable = Observable.create(new Observable.OnSubscribeFunc<ThreadRating>() {
//            @Override
//            public Subscription onSubscribe(Observer<? super ThreadRating> observer) {
//                return null;
//            }
//        });
//
//        storyObservable.join(ratingObservable, )

        return storyAndRatingObservable.toBlockingObservable().getIterator();
    }

    class GetThreadRatingCommand extends HystrixCommand<ThreadRating> {
        ThreadRatingDao dao;
        private Long userId;
        private Long threadId;

        GetThreadRatingCommand(ThreadRatingDao dao, Long userId, Long threadId) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ThreadRating")));
            this.dao = dao;
            this.userId = userId;
            this.threadId = threadId;
        }

        @Override
        protected ThreadRating run() throws Exception {
            return dao.getRatingsForUserAndThread(userId, threadId);
        }


    }
//
//    class ThreadRatingCollapser extends HystrixCollapser<List<ThreadRating>, ThreadRating, ThreadRatingAndUser> {
//        Long threadId;
//        Long userId;
//
//        public ThreadRatingCollapser(Long threadId, Long userId) {
//            super(HystrixCollapserKey.Factory.asKey("threadRating"));
//
//            this.threadId = threadId;
//            this.userId = userId;
//        }
//
//        @Override
//        public ThreadRatingAndUser getRequestArgument() {
//            return new ThreadRatingAndUser(threadId, userId);
//        }
//
//        @Override
//        protected HystrixCommand<List<ThreadRating>> createCommand(Collection<CollapsedRequest<ThreadRating, ThreadRatingAndUser>> collapsedRequests) {
//            Iterable<ThreadRatingAndUser> ratingAndUsers = Iterables.transform(collapsedRequests, new Function<CollapsedRequest<ThreadRating, ThreadRatingAndUser>, ThreadRatingAndUser>() {
//                @Override
//                public ThreadRatingAndUser apply(CollapsedRequest<ThreadRating, ThreadRatingAndUser> input) {
//                    return input.getArgument();
//                }
//            };
//
//            ratingDao.
//
//            return null;
//        }
//
//        @Override
//        protected void mapResponseToRequests(List<ThreadRating> threadRatings, Collection<CollapsedRequest<ThreadRating, ThreadRatingAndUser>> collapsedRequests) {
//
//        }
//    }

    class ThreadRatingAndUser {
        Long threadId;
        Long userId;

        ThreadRatingAndUser(Long threadId, Long userId) {
            this.threadId = threadId;
            this.userId = userId;
        }
    }

    @Data
    class StoryAndRating {
        final Story story;
        final ThreadRating rating;

        StoryAndRating(Story story, ThreadRating rating) {
            this.story = story;
            this.rating = rating;
        }
    }
//
//    @GET
//    @Metered
//    @Path("/{storyId}")
//    public Story getStory(@QueryParam("dlpThreadId") Long storyId) {
//        Story story = jacksonDBCollection.find(DBQuery.is(""));
//
//        if (story == null)
//            throw new WebApplicationException(Response.Status.NOT_FOUND);
//
//        return story;
//    }


    @ApiOperation("Gets a single story element")
    @GET
    @Metered
    @Path("/{storyId}")
    public Story getStory(@PathParam("storyId") Long storyId) {
        Story story = jacksonDBCollection.findOneById(storyId.toString());

        if (story == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);

        return story;
    }


    @ApiOperation("Gets multiple stories")
    @GET
    @Metered
    @Path("/multiget/{query}")
    public Iterator<Story> getMultipleStories(@PathParam("query") String query) {
        Splitter s = Splitter.on(",").omitEmptyStrings().trimResults().limit(50);

        List<Long> multiGet = Lists.newArrayList(Iterables.transform(s.split(query), new Function<String, Long>() {
            @Nullable
            @Override
            public Long apply(@Nullable String input) {
                return Long.parseLong(input);
            }
        }));

        log.info("Returning information about {}", multiGet);

        return jacksonDBCollection.find(DBQuery.in("_id", Lists.newArrayList(s.split(query))));
    }


    @ApiOperation("Gets stories with a particular tag")
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
