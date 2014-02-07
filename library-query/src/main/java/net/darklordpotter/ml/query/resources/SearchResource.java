package net.darklordpotter.ml.query.resources;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import net.darklordpotter.ml.query.Constants;
import net.darklordpotter.ml.query.api.SearchQuery;
import net.darklordpotter.ml.query.api.ffdb.ThreadData;
import net.darklordpotter.ml.query.api.ffdb.SearchResult;
import net.darklordpotter.ml.query.api.ffdb.StoryHeader;
import net.darklordpotter.ml.query.core.DateParam;
import net.darklordpotter.ml.query.core.StoryToThreadDataManager;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.phrase.PhraseSuggestionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.ws.rs.*;
import java.util.Collections;
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
            Iterable<String> ids = commaSplitter.split(id);

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
    @Path("/suggestions")
    public String suggestions() {
        PhraseSuggestionBuilder phraseSuggestionBuilder = new PhraseSuggestionBuilder("title_typeahead");
        phraseSuggestionBuilder.field("title");
        phraseSuggestionBuilder.text("hogwart");

        return client.prepareSuggest("ffn_index").addSuggestion(phraseSuggestionBuilder).get().getSuggest().toString();
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
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("ffn_index")
                .setTypes("story")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(query.toQueryBuilder())
                .setFrom(from)
                .setSize(max);

        if (!Strings.isNullOrEmpty(query.getSortBy()) && query.getSortBy().equalsIgnoreCase("_score")) {
            searchRequestBuilder.addSort(SortBuilders.scoreSort());
        } else {
            String orderBy = query.getOrderBy();
            if (Strings.isNullOrEmpty(orderBy) || (!orderBy.toLowerCase().equals("asc") && !orderBy.toLowerCase().equals("desc"))) orderBy = "ASC";
            searchRequestBuilder.addSort(query.getSortBy(), SortOrder.valueOf(orderBy.toUpperCase()));
            searchRequestBuilder.addSort(SortBuilders.scoreSort());
        }

        SearchResult result = SearchResult.fromResult(threadLinkSupplier, searchRequestBuilder.get());
        log.info("Query: {}", query);
        log.info("Query Results: {} hits, {} max score, completed in {} ms", result.getHits(), result.getMaxScore(), result.getTook());

        return result;
    }
}
