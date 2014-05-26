package net.darklordpotter.api.fiction.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.module.scala.DefaultScalaModule;
import lombok.AccessLevel;
import lombok.Getter;
import org.elasticsearch.search.SearchHit;
import org.joda.time.DateTime;

import java.util.Map;

/**
 * 2013-12-23
 *
 * @author Michael Rose
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoryHeader {
    @Getter(AccessLevel.NONE)
    static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.registerModule(new GuavaModule());
        mapper.registerModule(new DefaultScalaModule());
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
    }

    double score;

    long storyId;
    String title;
    String author;
    String authorUrl;
    long authorId;
    String summary;

    String url;
    String urlLatest;

    DateTime published;
    DateTime updated;

    StoryMeta meta;

    public StoryHeader() {
    }


    public static StoryHeader fromSource(SearchHit hit) {
        StoryHeader header = fromSource(hit.getSource());
        header.setScore(hit.score());

        return header;
    }

    public static StoryHeader fromSource(Map<String,Object> source) {
        StoryHeader header = mapper.convertValue(source, StoryHeader.class);

        return header;
    }

    public double getScore() {
        return this.score;
    }

    public long getStoryId() {
        return this.storyId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getAuthorUrl() {
        return this.authorUrl;
    }

    public long getAuthorId() {
        return this.authorId;
    }

    public String getSummary() {
        return this.summary;
    }

    public String getUrl() {
        return this.url;
    }

    public String getUrlLatest() {
        return this.urlLatest;
    }

    public DateTime getPublished() {
        return this.published;
    }

    public DateTime getUpdated() {
        return this.updated;
    }

    public StoryMeta getMeta() {
        return this.meta;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setStoryId(long storyId) {
        this.storyId = storyId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUrlLatest(String urlLatest) {
        this.urlLatest = urlLatest;
    }

    public void setPublished(DateTime published) {
        this.published = published;
    }

    public void setUpdated(DateTime updated) {
        this.updated = updated;
    }

    public void setMeta(StoryMeta meta) {
        this.meta = meta;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof StoryHeader)) return false;
        final StoryHeader other = (StoryHeader) o;
        if (!other.canEqual((Object) this)) return false;
        if (Double.compare(this.score, other.score) != 0) return false;
        if (this.storyId != other.storyId) return false;
        final Object this$title = this.title;
        final Object other$title = other.title;
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
        final Object this$author = this.author;
        final Object other$author = other.author;
        if (this$author == null ? other$author != null : !this$author.equals(other$author)) return false;
        final Object this$authorUrl = this.authorUrl;
        final Object other$authorUrl = other.authorUrl;
        if (this$authorUrl == null ? other$authorUrl != null : !this$authorUrl.equals(other$authorUrl)) return false;
        if (this.authorId != other.authorId) return false;
        final Object this$summary = this.summary;
        final Object other$summary = other.summary;
        if (this$summary == null ? other$summary != null : !this$summary.equals(other$summary)) return false;
        final Object this$url = this.url;
        final Object other$url = other.url;
        if (this$url == null ? other$url != null : !this$url.equals(other$url)) return false;
        final Object this$urlLatest = this.urlLatest;
        final Object other$urlLatest = other.urlLatest;
        if (this$urlLatest == null ? other$urlLatest != null : !this$urlLatest.equals(other$urlLatest)) return false;
        final Object this$published = this.published;
        final Object other$published = other.published;
        if (this$published == null ? other$published != null : !this$published.equals(other$published)) return false;
        final Object this$updated = this.updated;
        final Object other$updated = other.updated;
        if (this$updated == null ? other$updated != null : !this$updated.equals(other$updated)) return false;
        final Object this$meta = this.meta;
        final Object other$meta = other.meta;
        if (this$meta == null ? other$meta != null : !this$meta.equals(other$meta)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $score = Double.doubleToLongBits(this.score);
        result = result * PRIME + (int) ($score >>> 32 ^ $score);
        final long $storyId = this.storyId;
        result = result * PRIME + (int) ($storyId >>> 32 ^ $storyId);
        final Object $title = this.title;
        result = result * PRIME + ($title == null ? 0 : $title.hashCode());
        final Object $author = this.author;
        result = result * PRIME + ($author == null ? 0 : $author.hashCode());
        final Object $authorUrl = this.authorUrl;
        result = result * PRIME + ($authorUrl == null ? 0 : $authorUrl.hashCode());
        final long $authorId = this.authorId;
        result = result * PRIME + (int) ($authorId >>> 32 ^ $authorId);
        final Object $summary = this.summary;
        result = result * PRIME + ($summary == null ? 0 : $summary.hashCode());
        final Object $url = this.url;
        result = result * PRIME + ($url == null ? 0 : $url.hashCode());
        final Object $urlLatest = this.urlLatest;
        result = result * PRIME + ($urlLatest == null ? 0 : $urlLatest.hashCode());
        final Object $published = this.published;
        result = result * PRIME + ($published == null ? 0 : $published.hashCode());
        final Object $updated = this.updated;
        result = result * PRIME + ($updated == null ? 0 : $updated.hashCode());
        final Object $meta = this.meta;
        result = result * PRIME + ($meta == null ? 0 : $meta.hashCode());
        return result;
    }

    public boolean canEqual(Object other) {
        return other instanceof StoryHeader;
    }

    public String toString() {
        return "net.darklordpotter.api.fiction.core.StoryHeader(score=" + this.score + ", storyId=" + this.storyId + ", title=" + this.title + ", author=" + this.author + ", authorUrl=" + this.authorUrl + ", authorId=" + this.authorId + ", summary=" + this.summary + ", url=" + this.url + ", urlLatest=" + this.urlLatest + ", published=" + this.published + ", updated=" + this.updated + ", meta=" + this.meta + ")";
    }
}
