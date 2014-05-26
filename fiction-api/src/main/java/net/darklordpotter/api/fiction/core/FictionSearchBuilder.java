package net.darklordpotter.api.fiction.core;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FictionSearchBuilder {
    private final SearchQuery searchQuery;
    private ThreadLinker threadLinker;

    public FictionSearchBuilder(SearchQuery searchQuery, ThreadLinker threadLinker) {
        this.searchQuery = searchQuery;
        this.threadLinker = threadLinker;
    }

    public void addSort(SearchRequestBuilder searchRequestBuilder) {


        if (!Strings.isNullOrEmpty(searchQuery.getSortBy()) && searchQuery.getSortBy().equalsIgnoreCase("_score")) {
            searchRequestBuilder.addSort(SortBuilders.scoreSort());
        } else if (searchQuery.getSortBy().equalsIgnoreCase("_dlp")) {
            searchRequestBuilder.addSort(
                    SortBuilders
                            .scriptSort("dlp-score", "number")
                            .lang("native")
                            .order(SortOrder.valueOf(searchQuery.getOrderBy().toUpperCase()))
            );
        } else if (searchQuery.getSortBy().equalsIgnoreCase("_popular")) {
            searchRequestBuilder.addSort(SortBuilders.scoreSort());
        } else {
            if (Strings.isNullOrEmpty(searchQuery.getOrderBy()) || (!searchQuery.getOrderBy().toLowerCase().equals("asc") && !searchQuery.getOrderBy().toLowerCase().equals("desc")))
                searchQuery.setOrderBy("ASC");
            searchRequestBuilder.addSort(searchQuery.getSortBy(), SortOrder.valueOf(searchQuery.getOrderBy().toUpperCase()));
            searchRequestBuilder.addSort(SortBuilders.scoreSort());
        }

    }

    public QueryBuilder toQueryBuilder() {
        BoolQueryBuilder query = QueryBuilders.boolQuery();

        queryString(query, "title", searchQuery.getTitle());
        queryString(query, "author", searchQuery.getAuthor());
        queryString(query, "summary", searchQuery.getSummary());
        range(query, "meta.words", searchQuery.getWordcountLower(), searchQuery.getWordcountUpper());
        range(query, "meta.chapters", searchQuery.getChaptersLower(), searchQuery.getChaptersUpper());

        List<FilterBuilder> filterBuilders = Lists.newArrayList();
        filterBuilders.add(filter("meta.characters.character_id", searchQuery.getCharacterRequired(), true, false));
        filterBuilders.add(filter("meta.characters.character_id", searchQuery.getCharacterOptional(), false, searchQuery.isCharacterOptionalExclude()));
        filterBuilders.add(filter("meta.categories.category_id", searchQuery.getCategoryRequired(), true, false));
        filterBuilders.add(filter("meta.categories.category_id", searchQuery.getCategoryOptional(), false, searchQuery.isCategoryOptionalExclude()));
        filterBuilders.add(filter("meta.language", searchQuery.getLanguage().toLowerCase(), false));

        if (searchQuery.getRating().size() < 4)
            filterBuilders.add(filter("meta.rated", searchQuery.getRating(), false, false));

        if (searchQuery.getSortBy().equalsIgnoreCase("_dlp") && threadLinker != null) {
            filterBuilders.add(filter("_id", threadLinker.storyIds(), false, false));
            return QueryBuilders.functionScoreQuery(filter("_id", threadLinker.storyIds(), false, false),
                    ScoreFunctionBuilders.scriptFunction("dlp-score", "native")
            );
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


        if (searchQuery.getSortBy().equalsIgnoreCase("_popular")) {
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
        if (!Strings.isNullOrEmpty(value)) {
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