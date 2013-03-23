package net.darklordpotter.mobile.library.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Sets;

import java.util.Date;
import java.util.Set;

/**
 * 2013-03-14
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Story {
    public Story(Long threadId) {
        this.threadId = threadId;
    }

    public Story(String threadId) {
        this.threadId = Long.parseLong(threadId);
    }

    public Story() {
    }

    public String getThreadId() {
        return threadId.toString();
    }

    public void setThreadId(String id) {
        this.threadId = Long.parseLong(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Story");
        sb.append("{threadId=").append(threadId);
        sb.append(", title='").append(title).append("'");
        sb.append(", author='").append(author).append("'");
        sb.append(", summary='").append(summary).append("'");
//        sb.append(", rating=").append(rating);
        sb.append(", threadRating=").append(threadRating);
        sb.append(", posted=").append(posted);
//        sb.append(", url=").append(url);
        sb.append(", tags=").append(tags);
        sb.append("}");
        return sb.toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
//
//    public Rating getRating() {
//        return rating;
//    }
//
//    public void setRating(Rating rating) {
//        this.rating = rating;
//    }

    public Double getThreadRating() {
        return threadRating;
    }

    public void setThreadRating(Double threadRating) {
        this.threadRating = threadRating;
    }

    public Date getPosted() {
        return posted;
    }

    public void setPosted(Date posted) {
        this.posted = posted;
    }

//    public Set<Url> getUrl() {
//        return url;
//    }
//
//    public void setUrl(Set<Url> url) {
//        this.url = url;
//    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    private Long threadId = 0l;
    private String title = "";
    private String author = "";
    private String summary = "";
    private Double threadRating = 0.0;
    private Date posted;
    private Set<String> tags = Sets.newHashSet();
}