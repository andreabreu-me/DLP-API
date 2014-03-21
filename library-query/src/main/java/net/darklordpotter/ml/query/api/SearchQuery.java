package net.darklordpotter.ml.query.api;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import net.darklordpotter.ml.query.api.ffdb.ThreadData;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * 2014-01-30
 *
 *       params =
 title: $("#title").val()
 author: $("#author").val()
 summary: $("#summary").val()
 wordcount_lower: $("#wordcount_lower").val()
 wordcount_upper: $("#wordcount_upper").val()
 rating: $scope.rating
 character_required: $scope.character_required
 character_optional_exclude: $scope.character_optional_exclude
 character_optional: $scope.character_optional
 genre_required: $scope.genre_required
 genre_optional_exclude: $scope.genre_optional_exclude
 genre_optional: $scope.genre_optional
 language: $scope.language
 sort_by: $scope.sort_by
 order_by: $scope.order_by

 *
 * @author Michael Rose
 */
@Data
public class SearchQuery {
    @Getter(AccessLevel.NONE)
    private final Logger log = LoggerFactory.getLogger(SearchQuery.class);

    String title, author, summary;
    Integer wordcountLower;
    Integer wordcountUpper;
    Integer chaptersLower;
    Integer chaptersUpper;

    List<String> rating = Collections.emptyList();
    List<Integer> categoryRequired = Collections.emptyList();
    List<Integer> categoryOptional = Collections.emptyList();

    boolean categoryOptionalExclude;
    List<Integer> characterRequired = Collections.emptyList();
    List<Integer> characterOptional = Collections.emptyList();

//    List<List<Integer>> pairings = Collections.emptyList();

    boolean characterOptionalExclude;
    String language;
    String sortBy, orderBy;
    
    public void addSort(SearchRequestBuilder searchRequestBuilder) {


        if (!Strings.isNullOrEmpty(sortBy) && sortBy.equalsIgnoreCase("_score")) {
            searchRequestBuilder.addSort(SortBuilders.scoreSort());
        } else if (sortBy.equalsIgnoreCase("_dlp")) {
            searchRequestBuilder.addSort(
                    SortBuilders
                            .scriptSort("dlp-score", "number")
                            .lang("native")
                            .order(SortOrder.valueOf(orderBy.toUpperCase()))
            );
        } else if (sortBy.equalsIgnoreCase("_popular")) {
            searchRequestBuilder.addSort(SortBuilders.scoreSort());
        } else {
            if (Strings.isNullOrEmpty(orderBy) || (!orderBy.toLowerCase().equals("asc") && !orderBy.toLowerCase().equals("desc"))) orderBy = "ASC";
            searchRequestBuilder.addSort(sortBy, SortOrder.valueOf(orderBy.toUpperCase()));
            searchRequestBuilder.addSort(SortBuilders.scoreSort());
        }

    }

    public QueryBuilder toQueryBuilder(Map<Long, ThreadData> dlpThreadLinks) {
        BoolQueryBuilder query = QueryBuilders.boolQuery();

        queryString(query, "title", title);
        queryString(query, "author", author);
        queryString(query, "summary", summary);
        range(query, "meta.words", wordcountLower, wordcountUpper);
        range(query, "meta.chapters", chaptersLower, chaptersUpper);

        List<FilterBuilder> filterBuilders = Lists.newArrayList();
        filterBuilders.add(filter("meta.characters.character_id", characterRequired, true, false));
        filterBuilders.add(filter("meta.characters.character_id", characterOptional, false, characterOptionalExclude));
        filterBuilders.add(filter("meta.categories.category_id", categoryRequired, true, false));
        filterBuilders.add(filter("meta.categories.category_id", categoryOptional, false, categoryOptionalExclude));
        filterBuilders.add(filter("meta.language", language.toLowerCase(), false));

        if (rating.size() < 4)
            filterBuilders.add(filter("meta.rated", rating, false, false));

        if (sortBy.equalsIgnoreCase("_dlp")) {
            filterBuilders.add(filter("_id", dlpThreadLinks.keySet(), false, false));
//            return QueryBuilders.functionScoreQuery(filter("_id", dlpThreadLinks.keySet(), false, false),
//                    ScoreFunctionBuilders.scriptFunction("dlp-score", "native")
//            );
        }

        List<FilterBuilder> nonNullFilters = Lists.newArrayList();
        for (FilterBuilder filterBuilder : filterBuilders) {
            if (filterBuilder != null) {
                nonNullFilters.add(filterBuilder);
            }
        }


        FilteredQueryBuilder filteredQuery = QueryBuilders.filteredQuery(
                query,
                FilterBuilders.andFilter((FilterBuilder[]) nonNullFilters.toArray(new FilterBuilder[0]))
        );


        if (sortBy.equalsIgnoreCase("_popular")) {
            FunctionScoreQueryBuilder builder = QueryBuilders.functionScoreQuery(filteredQuery);
            builder.add(ScoreFunctionBuilders.scriptFunction("_score + (" +
                    "(doc['meta.follows'].empty ? 0 : doc['meta.follows'].value) + " +
                    "(doc['meta.favs'].empty ? 0 : doc['meta.favs'].value) + " +
                    "(doc['meta.reviews'].empty ? 0 : doc['meta.reviews'].value)" +
                    ") / pow(max(1,doc['meta.chapters'].value), 0.5)"));
            builder.add(ScoreFunctionBuilders.scriptFunction("1/((time() - doc['updated'].value)/1000/86400/30)"));
//            builder.add(ScoreFunctionBuilders.scriptFunction("activity", "native"));
//            builder.add(ScoreFunctionBuilders.scriptFunction("freshness", "native"));

            return builder;
        }


        return filteredQuery;
    }

    private void queryString(BoolQueryBuilder query, String field, String value) {
        if (!isNullOrEmpty(value)) {
            query.must(QueryBuilders.queryString(value.replace("/", "\\/")).field(field));
        }
    }

    private void range(BoolQueryBuilder query, String field, Integer lower, Integer upper) {
        // Null in from/to represents 'unbounded'
        query.must(QueryBuilders.rangeQuery(field).from(lower).to(upper));
    }

    private <T> FilterBuilder filter(String field, T element, boolean exclude) {
        return filter(field, Collections.singletonList(element), true, exclude);
    }

    private <T> FilterBuilder filter(String field, Collection<T> inElements, boolean matchAll, boolean exclude) {
        if (inElements.isEmpty()) return null;

        TermsFilterBuilder filterBuilder = FilterBuilders.inFilter(field, inElements.toArray());

        if (matchAll) filterBuilder.execution("and");

        if (exclude) {
            return FilterBuilders.notFilter(filterBuilder);
        } else {
            return filterBuilder;
        }
    }
}
