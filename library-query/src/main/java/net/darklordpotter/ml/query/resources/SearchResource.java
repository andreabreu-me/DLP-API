//package net.darklordpotter.ml.query.resources;
//
//import com.google.common.base.Splitter;
//import com.wordnik.swagger.annotations.Api;
//import com.wordnik.swagger.annotations.ApiOperation;
//import net.darklordpotter.api.fiction.core.*;
//import net.darklordpotter.api.fiction.search.ScryerSearcher;
//import net.darklordpotter.api.fiction.search.Searcher;
//import net.darklordpotter.ml.query.Constants;
//import net.darklordpotter.ml.query.core.DateParam;
//import org.elasticsearch.action.search.SearchRequestBuilder;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.client.Client;
//import org.elasticsearch.index.query.FilterBuilders;
//import org.elasticsearch.search.sort.SortOrder;
//import org.joda.time.DateTime;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import scala.Option;
//
//import javax.ws.rs.*;
//
///**
//* 2013-12-23
//*
//* @author Michael Rose <lordravenclaw@patronuscharm.net>
//*/
//@Api(value = "/v1/search", description = "Search API for the Scryer Fanfiction Database")
//@Path("/v1/search")
//@Produces(Constants.APPLICATION_JSON)
//public class SearchResource {
//    private final Client client;
//    private final Splitter commaSplitter = Splitter.on(',').trimResults();
//    private final ThreadLinker threadLinker;
//    private Logger log = LoggerFactory.getLogger(SearchResource.class);
//
//    Searcher searcher;
//
//    public SearchResource(Client client) {
//        this.client = client;
//        this.threadLinker = new ThreadLinker(StoryToThreadDataManager.threadLinkSupplier());
//        this.searcher = new ScryerSearcher(client, threadLinker);
//    }
//
//    @ApiOperation(
//            value = "Retrieve story from search index by id",
//            notes = "Comma-separating ids will perform a multi-fetch",
//            response = StoryHeader.class)
//
//
//@GET
//    @Path("/{id: [0-9,]+}")
//    public Iterable<StoryHeader> getById(@PathParam("id") String id) {
//        return (Iterable<StoryHeader>)searcher.fetch(scalaList(commaSplitter.split(id)))
//                .asJavaObservable()
//                .toBlockingObservable()
//                .toIterable();
//    }
//
//    public static <T> scala.collection.immutable.List<T> scalaList(Iterable<T> javaList) {
//        return scala.collection.JavaConversions.asScalaIterable(javaList).toList();
//    }
//
//    @GET
//    @Path("/updates")
//    public SearchResult updatesSince(@QueryParam("since") DateParam dateParam,
//                                              @QueryParam("from") @DefaultValue("0") int from,
//                                              @QueryParam("max") @DefaultValue("50") int max) {
//        if (max > 250) max = 250;
////
////        SearchRequestBuilder search = client.prepareSearch("ffn_index")
////                .addSort("updated", SortOrder.DESC)
////                .setFrom(from)
////                .setSize(max);
////
////        if (dateParam != null) {
////            search.setFilter(FilterBuilders.rangeFilter("updated").gte(dateParam.get()));
////        }
//
//        return searcher.updatesSince(Option.apply(dateParam.get()), from, max)
//                       .asJavaObservable()
//                       .toBlockingObservable()
//                       .single();
//    }
//
//
//    @ApiOperation(
//            value = "Search the story index for a given SearchQuery",
//            response = SearchResult.class)
//    @POST
//    @Path("/")
//    public SearchResult query(
//                                SearchQuery query,
//                                @QueryParam("from") @DefaultValue("0") int from,
//                                @QueryParam("max") @DefaultValue("50") int max) {
//        if (max > 250) max = 250;
//
//
//        SearchResult result = searcher.query(query, from, max)
//                                      .asJavaObservable()
//                                      .toBlockingObservable()
//                                      .single();
////
////        if (true)
////            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(result).build());
//        log.info("Query: {}", query);
//        log.info("Query Results: {} hits, {} max score, completed in {} ms", result.getHits(), result.getMaxScore(), result.getTook());
//
//        return result;
//    }
//}
