package net.darklordpotter.ml.query.api;

import com.google.common.collect.Lists;
import lombok.Data;
import org.elasticsearch.index.query.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
 * @author Michael Rose <michael@fullcontact.com>
 */
@Data
public class SearchQuery {
    private Logger log = LoggerFactory.getLogger(SearchQuery.class);

    String title, author, summary;
    Integer wordcountLower;

    Integer wordcountUpper;

    List<String> rating = Collections.emptyList();
    List<Integer> genreRequired = Collections.emptyList();
    List<Integer> genreOptional = Collections.emptyList();

    boolean genreOptionalExclude;
    List<Integer> characterRequired = Collections.emptyList();
    List<Integer> characterOptional = Collections.emptyList();

    boolean characterOptionalExclude;
    String language;
    String sortBy, orderBy;

    public boolean isFreeText() {
        return !isNullOrEmpty(title) || !isNullOrEmpty(summary);
    }

    public QueryBuilder toQueryBuilder() {
        BoolQueryBuilder query = QueryBuilders.boolQuery();

        match(query, "title", title);
        match(query, "author", author);
        match(query, "summary", summary);
        range(query, "meta.words", wordcountLower, wordcountUpper);

        List<FilterBuilder> filterBuilders = Lists.newArrayList();
        filterBuilders.add(filter("meta.characters.character_id", characterRequired, true, false));
        filterBuilders.add(filter("meta.characters.character_id", characterOptional, false, characterOptionalExclude));
        filterBuilders.add(filter("meta.genres.genres_id", genreRequired, true, false));
        filterBuilders.add(filter("meta.genres.genres_id", genreOptional, false, genreOptionalExclude));
//        filterBuilders.add(filter("meta.language", language, false));

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

        log.info("From data {} ", this);

        return filteredQuery;
    }

    private void match(BoolQueryBuilder query, String field, String value) {
        if (!isNullOrEmpty(value)) {
            query.must(QueryBuilders.matchQuery(field, value));
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
