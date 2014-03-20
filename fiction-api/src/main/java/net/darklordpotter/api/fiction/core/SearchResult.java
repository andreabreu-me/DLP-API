package net.darklordpotter.api.fiction.core;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import java.util.List;
import java.util.Map;

/**
 * 2013-12-23
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
public class SearchResult {
    long took;
    long hits;
    double maxScore;
    String reason;

    List<StoryHeader> results = Lists.newArrayList();

    public SearchResult() {
    }

    public static SearchResult fromResult(SearchResponse response, ThreadLinker threadLinker) {
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
                header.setMeta(
                        header.getMeta().addThreadData(threadLinker.findThreadData(header.getStoryId()))
                );

                result.results.add(header);
            }
        }

        return result;
    }

    public long getTook() {
        return this.took;
    }

    public long getHits() {
        return this.hits;
    }

    public double getMaxScore() {
        return this.maxScore;
    }

    public String getReason() {
        return this.reason;
    }

    public List<StoryHeader> getResults() {
        return this.results;
    }

    public void setTook(long took) {
        this.took = took;
    }

    public void setHits(long hits) {
        this.hits = hits;
    }

    public void setMaxScore(double maxScore) {
        this.maxScore = maxScore;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setResults(List<StoryHeader> results) {
        this.results = results;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SearchResult)) return false;
        final SearchResult other = (SearchResult) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.took != other.took) return false;
        if (this.hits != other.hits) return false;
        if (Double.compare(this.maxScore, other.maxScore) != 0) return false;
        final Object this$reason = this.reason;
        final Object other$reason = other.reason;
        if (this$reason == null ? other$reason != null : !this$reason.equals(other$reason)) return false;
        final Object this$results = this.results;
        final Object other$results = other.results;
        if (this$results == null ? other$results != null : !this$results.equals(other$results)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $took = this.took;
        result = result * PRIME + (int) ($took >>> 32 ^ $took);
        final long $hits = this.hits;
        result = result * PRIME + (int) ($hits >>> 32 ^ $hits);
        final long $maxScore = Double.doubleToLongBits(this.maxScore);
        result = result * PRIME + (int) ($maxScore >>> 32 ^ $maxScore);
        final Object $reason = this.reason;
        result = result * PRIME + ($reason == null ? 0 : $reason.hashCode());
        final Object $results = this.results;
        result = result * PRIME + ($results == null ? 0 : $results.hashCode());
        return result;
    }

    public boolean canEqual(Object other) {
        return other instanceof SearchResult;
    }

    public String toString() {
        return "net.darklordpotter.api.fiction.core.SearchResult(took=" + this.took + ", hits=" + this.hits + ", maxScore=" + this.maxScore + ", reason=" + this.reason + ", results=" + this.results + ")";
    }
}
