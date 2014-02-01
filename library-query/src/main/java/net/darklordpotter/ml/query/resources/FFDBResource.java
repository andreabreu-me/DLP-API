package net.darklordpotter.ml.query.resources;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.darklordpotter.ml.query.Constants;
import net.darklordpotter.ml.query.api.SearchQuery;
import net.darklordpotter.ml.query.api.ffdb.SearchResult;
import net.darklordpotter.ml.query.api.ffdb.StoryHeader;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.facet.FacetBuilder;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import scala.annotation.meta.field;
import scala.annotation.meta.param;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.*;

/**
 * 2013-12-23
 *
 * @author Michael Rose <michael@fullcontact.com>
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
    public SearchResult query(SearchQuery query, @QueryParam("from") @DefaultValue("0") int from,
    @QueryParam("max") @DefaultValue("25") int max) {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("ffn_index")
                .setTypes("story")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(query.toQueryBuilder())
                .setFrom(from)
                .setSize(max);

        return SearchResult.fromResult(searchRequestBuilder.get());
    }

    @GET
    @Path("/search")
    public SearchResult searchQuery(@Context UriInfo uriInfo,
                                    @QueryParam("from") @DefaultValue("0") int from,
                                    @QueryParam("max") @DefaultValue("25") int max) {
//        if (queryParam == null) throw new WebApplicationException(Response.Status.BAD_REQUEST);


        QueryBuilder queryBuilder = buildQuery(uriInfo.getQueryParameters());
        SortBuilder sortBuilder = buildSort(uriInfo.getQueryParameters());

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("ffn_index")
                .setTypes("story")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .setFrom(from)
                .setSize(max);

        if (sortBuilder != null) {
            // If have freeform search fields, show the best match rather than honoring sorting
            if (Sets.intersection(uriInfo.getQueryParameters().keySet(), shouldFields).size() > 0) {
                searchRequestBuilder.addSort(SortBuilders.scoreSort());
                searchRequestBuilder.addSort(sortBuilder);
            } else {
                searchRequestBuilder.addSort(sortBuilder);
                searchRequestBuilder.addSort(SortBuilders.scoreSort());
            }
        }

        System.out.println(searchRequestBuilder.toString());

        return SearchResult.fromResult(searchRequestBuilder.get());
    }

    private SortBuilder buildSort(MultivaluedMap<String, String> queryParameters) {
        SortBuilder sortBuilder = null;

        if (queryParameters.containsKey("sort_by")) {
            sortBuilder = SortBuilders.fieldSort(queryParameters.getFirst("sort_by").toLowerCase());

            if (queryParameters.containsKey("order_by")) {
                sortBuilder.order(SortOrder.valueOf(queryParameters.getFirst("order_by").toUpperCase()));
            }
        }

        return sortBuilder;
    }

    private QueryBuilder buildQuery(MultivaluedMap<String, String> queryParameters) {
        BoolQueryBuilder query = QueryBuilders.boolQuery();

        for (Map.Entry<String,List<String>> entry : queryParameters.entrySet()) {
            utilizeShouldFields(query, entry);
        }

        utilizeRangeFields(query, queryParameters);
        utilizeFilterFields(query, queryParameters);

        return query;
    }

    Splitter comma = Splitter.on(',').trimResults().omitEmptyStrings();
    private void utilizeFilterFields(BoolQueryBuilder query, MultivaluedMap<String, String> queryParameters) {
        mustMultiple(query, queryParameters, "meta.rated", "rating", false, false);

        mustMultiple(query, queryParameters, "meta.characters.character_id", "character_required", false, true);
        boolean excludeOptionalCharacters = queryParameters.getFirst("character_optional_exclude").toLowerCase().equals("exclude");
        mustMultiple(query, queryParameters, "meta.characters.character_id", "character_optional", excludeOptionalCharacters, false);

        mustMultiple(query, queryParameters, "meta.genres.genre_id", "genre_required", false, true);
        boolean excludeOptionalGenres = queryParameters.getFirst("genre_optional_exclude").toLowerCase().equals("exclude");
        mustMultiple(query, queryParameters, "meta.genres.genre_id", "genre_optional", excludeOptionalGenres, true);
    }

    private void mustMultiple(BoolQueryBuilder query, MultivaluedMap<String, String> queryParameters, String field, String param, boolean exclude, boolean all) {
        if (queryParameters.containsKey(param)) {
            TermsQueryBuilder inQuery = buildMultipleTermQuery(queryParameters, field, param, all);

            if (exclude)
                query.mustNot(inQuery);
            else
                query.must(inQuery);
        }
    }

    private TermsQueryBuilder buildMultipleTermQuery(MultivaluedMap<String, String> queryParameters, String field, String param, boolean all) {
        String[] terms = Iterables.toArray(
                comma.split(queryParameters.getFirst(param).toLowerCase()),
                String.class
        );
        TermsQueryBuilder termsQueryBuilder = QueryBuilders.inQuery(field, terms);

        if (all) termsQueryBuilder.minimumMatch(terms.length);

        return termsQueryBuilder;
    }

    private void utilizeRangeFields(BoolQueryBuilder query, MultivaluedMap<String, String> queryParameters) {
        if (queryParameters.containsKey("wordcount_lower") || queryParameters.containsKey("wordcount_upper")) {
            String lower = queryParameters.getFirst("wordcount_lower");
            String upper = queryParameters.getFirst("wordcount_upper");

            queryParameters.remove("wordcount_lower");
            queryParameters.remove("wordcount_upper");

            if (lower != null && upper != null) {
                query.must(QueryBuilders.rangeQuery("meta.words").from(lower).to(upper));
            } else if (lower == null && upper != null) {
                query.must(QueryBuilders.rangeQuery("meta.words").lte(upper));
            } else if (lower != null && upper == null) {
                query.must(QueryBuilders.rangeQuery("meta.words").gte(lower));
            }
        }
    }

    Set<String> shouldFields = Sets.newHashSet(
            "title", "author", "summary"
    );
    private void utilizeShouldFields(BoolQueryBuilder query, Map.Entry<String, List<String>> entry) {
        if (!shouldFields.contains(entry.getKey()) || entry.getValue().get(0).isEmpty()) return;

        query.must(QueryBuilders.matchQuery(entry.getKey(), entry.getValue().get(0)));
    }

    private void utilizeFilterFields(BoolQueryBuilder query, Map.Entry<String, List<String>> entry) {
        if (!filterFields.contains(entry.getKey())) return;

        query.should(QueryBuilders.filteredQuery(query, FilterBuilders.))

        query.should(QueryBuilders.fieldQuery(entry.getKey(), entry.getValue().get(0)));
    }
//
    @GET
    @Path("/search/faceted")
    public String searchQueryFaceted(@QueryParam("q") String queryParam) {
        if (queryParam == null) throw new WebApplicationException(Response.Status.BAD_REQUEST);


        return client.prepareSearch("ffn_index").setTypes("story").setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                        .setQuery(QueryBuilders.queryString(queryParam))
                        .addFacet(FacetBuilders.termsFacet("f").field("summary").size(10).global(false))
                        .execute().actionGet().toString();
    }
}
