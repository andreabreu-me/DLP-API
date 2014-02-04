package net.darklordpotter.ml.query.resources;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import net.darklordpotter.ml.query.Constants;
import net.darklordpotter.ml.query.api.SearchQuery;
import net.darklordpotter.ml.query.api.ffdb.SearchResult;
import net.darklordpotter.ml.query.api.ffdb.StoryHeader;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import javax.ws.rs.*;
import java.util.Collections;

/**
 * 2013-12-23
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
@Path("/ffdb")
@Produces(Constants.APPLICATION_JSON)
public class FFDBResource {
    private final Client client;
    private final Splitter commaSplitter = Splitter.on(',').trimResults();

    public FFDBResource(Client client) {
        this.client = client;
    }

    @GET
    @Path("/id/{id}")
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

    @POST
    @Path("/search")
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

        System.out.println(searchRequestBuilder);

        return SearchResult.fromResult(searchRequestBuilder.get());
    }
}
