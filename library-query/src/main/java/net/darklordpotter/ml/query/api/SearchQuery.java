package net.darklordpotter.ml.query.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;

import java.util.Collections;
import java.util.List;

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
    String title, author, summary;

    @JsonProperty("wordcount_lower")
    Integer wordcountLower;

    @JsonProperty("wordcount_upper")
    Integer wordcountUpper;
    @JsonProperty("character_required")
    List<Integer> characterRequired = Collections.emptyList();
    @JsonProperty("character_optional")
    List<Integer> characterOptional = Collections.emptyList();

    @JsonProperty("genre_required")
    List<Integer> genreRequired = Collections.emptyList();

    @JsonProperty("genre_optional")
    List<Integer> genreOptional = Collections.emptyList();
    String language;
    String sortBy, orderBy;

    public BoolQueryBuilder toQueryBuilder() {
        BoolQueryBuilder query = QueryBuilders.boolQuery();

        term(query, "title", title);
        term(query, "author", author);
        term(query, "summary", summary);
        range(query, "meta.words", wordcountLower, wordcountUpper);
        filter(query, "meta.characters.character_id", characterRequired, true);
        filter(query, "meta.genres.genres_id", genreRequired, true);
        filter(query, "meta.language", language);

        System.out.println(query);

        return query;
    }

    private <T> void term(BoolQueryBuilder query, String field, T value) {
        if (value != null) {
            query.must(QueryBuilders.termQuery(field, value));
        }
    }

    private void range(BoolQueryBuilder query, String field, Integer lower, Integer upper) {
        // Null in from/to represents 'unbounded'
        query.must(QueryBuilders.rangeQuery(field).from(lower).to(upper));
    }

    private <T> void filter(BoolQueryBuilder query, String field, T element) {
    }

    private <T> void filterNot(BoolQueryBuilder query, String field, T value) {
        if (value != null) {
            query.mustNot(QueryBuilders.termQuery(field, value));
        }
    }

    private <T> void filter(BoolQueryBuilder query, String field, List<T> inElements, boolean matchAll) {
        if (inElements.isEmpty()) return;

        TermsQueryBuilder queryBuilder = QueryBuilders.inQuery(field, inElements);
        if (matchAll) queryBuilder.minimumMatch(inElements.size());
        query.must(queryBuilder);
    }

    /**
     * Does an exclusion TermsQuery
     *
     * @param query query builder
     * @param field document field
     * @param inElements
     * @param matchAll If the document does not match ALL terms, exclude. Otherwise a document matching some terms will
     *                 still be allowed
     * @param <T>
     */
    private <T> void filterNot(BoolQueryBuilder query, String field, List<T> inElements, boolean matchAll) {
        if (inElements.isEmpty()) return;

        TermsQueryBuilder queryBuilder = QueryBuilders.inQuery(field, inElements);
        if (matchAll) queryBuilder.minimumMatch(inElements.size());
        query.mustNot(queryBuilder);
    }
}
