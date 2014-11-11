package net.darklordpotter.ml.query.resources;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.yammer.metrics.annotation.Metered;
import net.darklordpotter.ml.core.Story;
import net.darklordpotter.ml.query.Constants;
import net.darklordpotter.ml.query.api.SearchQuery;
import net.darklordpotter.ml.query.api.ffdb.*;
import net.darklordpotter.ml.query.api.ffdb.Character;
import net.darklordpotter.ml.query.core.DateParam;
import net.darklordpotter.ml.query.core.StoryToThreadDataManager;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.phrase.PhraseSuggestionBuilder;
import org.elasticsearch.transport.TransportSerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 2013-12-23
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
@Api(value = "/v1/search", description = "Search API for the Scryer Fanfiction Database")
@Path("/v1/search")
@Produces(Constants.APPLICATION_JSON)
public class SearchResource {
    private final Client client;
    private final Splitter commaSplitter = Splitter.on(',').trimResults();
    private final Supplier<Map<Long, ThreadData>> threadLinkSupplier;
    private Logger log = LoggerFactory.getLogger(SearchResource.class);

    public SearchResource(Client client) {
        this.client = client;
        this.threadLinkSupplier = StoryToThreadDataManager.threadLinkSupplier();
    }

    @ApiOperation(
            value = "Retrieve story from search index by id",
            notes = "Comma-separating ids will perform a multi-fetch",
            response = StoryHeader.class)
    @GET
    @Path("/{id: [0-9,]+}")
    public Iterable<StoryHeader> getById(@PathParam("id") String id) {
        if (id.contains(",")) {
            List<String> ids = Lists.newArrayList(commaSplitter.split(id));

            if (ids.size() > 100) ids = ids.subList(0, 99);

            return Iterables.transform(
                    client.prepareMultiGet().add("ffn_index", "story", ids).get(),
                    new Function<MultiGetItemResponse, StoryHeader>() {
                        @Override
                        public StoryHeader apply(MultiGetItemResponse input) {
                            return StoryHeader.fromSource(input.getResponse().getSource());
                        }
                    });
        } else {
            return Collections.singletonList(
                    StoryHeader.fromSource(client.prepareGet("ffn_index", "story", id).get().getSource())
            );
        }
    }

    @GET
    @Path("/updates")
    public SearchResult updatesSince(@QueryParam("since") DateParam dateParam,
                                              @QueryParam("from") @DefaultValue("0") int from,
                                              @QueryParam("max") @DefaultValue("50") int max) {
        if (max > 250) max = 250;

        SearchRequestBuilder search = client.prepareSearch("ffn_index")
                .addSort("updated", SortOrder.DESC)
                .setFrom(from)
                .setSize(max);

        if (dateParam != null) {
            search.setFilter(FilterBuilders.rangeFilter("updated").gte(dateParam.get()));
        }

        SearchResponse response = search.get();
        return SearchResult.fromResult(threadLinkSupplier, response);
    }


    @ApiOperation(
            value = "Search the story index for a given SearchQuery",
            response = SearchResult.class)
    @POST
    @Path("/")
    public SearchResult query(
                                SearchQuery query,
                                @QueryParam("from") @DefaultValue("0") int from,
                                @QueryParam("max") @DefaultValue("50") int max) {
        if (max > 250) max = 250;

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("ffn_index")
                .setTypes("story")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(query.toQueryBuilder(threadLinkSupplier.get()))
                .setFrom(from)
                .setSize(max);

        if (query.getSortBy().equalsIgnoreCase("_popular")) {
            searchRequestBuilder.setSearchType(SearchType.QUERY_THEN_FETCH);
        }

        query.addSort(searchRequestBuilder);

        SearchResponse response = null;
        try {
            response = searchRequestBuilder.get();
        } catch (SearchPhaseExecutionException e) {

        } catch (TransportSerializationException tse) {
            log.error("Wat" + tse.getMessage());
        } catch (Exception e) {
            log.error("Error searching!", e);
        }

        SearchResult result = SearchResult.fromResult(threadLinkSupplier, response);
//
//        if (true)
//            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(result).build());
        log.info("Query: {}", query);
        log.info("Query Results: {} hits, {} max score, completed in {} ms", result.getHits(), result.getMaxScore(), result.getTook());

        return result;
    }
}
