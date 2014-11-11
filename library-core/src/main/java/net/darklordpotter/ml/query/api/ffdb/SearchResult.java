package net.darklordpotter.ml.query.api.ffdb;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import lombok.Data;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import java.util.List;
import java.util.Map;

/**
 * 2013-12-23
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
@Data
public class SearchResult {
    long took;
    long hits;
    double maxScore;
    String reason;

    List<StoryHeader> results = Lists.newArrayList();

    public static SearchResult fromResult(Supplier<Map<Long,ThreadData>> threadLinkSupplier, SearchResponse response) {
//        System.out.println(response);
        SearchResult result = new SearchResult();

        if (response != null) {
            result.took = response.getTookInMillis();

            if (response.getHits() != null) {
                result.hits = response.getHits().totalHits();
                result.maxScore = response.getHits().maxScore();
            }

            if (response.getFailedShards() > 0) {
                result.reason = response.getShardFailures()[0].reason();
            }

            for (SearchHit hit : response.getHits().hits()) {
                StoryHeader header = StoryHeader.fromSource(hit);
                header.getMeta().setThreadData(threadLinkSupplier.get().get(header.getStoryId()));
                result.results.add(header);
            }
        }

        return result;
    }
}
