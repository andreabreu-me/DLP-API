package net.darklordpotter.ml.core

import com.google.common.collect.Sets

import javax.persistence.Id

/**
 * 2013-02-07
 * @author Michael Rose <michael@fullcontact.com>
 */
public class Story {
    private Long threadId = 0
    String title = ""
    String author = ""
    String summary = ""
    Rating rating
    Double threadRating = 0.0
    Set<Url> url = Sets.newHashSet()
    Set<String> tags = Sets.newHashSet()

    Story(Long threadId) {
        this.threadId = threadId
    }


    Story(String threadId) {
        this.threadId = Long.parseLong(threadId)
    }


    Story() {}

    @Id
    public String getThreadId() {
        return threadId.toString()
    }

    @Id
    void setThreadId(String id) {
        this.threadId = Long.parseLong(id)
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Story");
        sb.append("{threadId=").append(threadId);
        sb.append(", title='").append(title).append('\'');
        sb.append(", author='").append(author).append('\'');
        sb.append(", summary='").append(summary).append('\'');
        sb.append(", rating=").append(rating);
        sb.append(", threadRating=").append(threadRating);
        sb.append(", url=").append(url);
        sb.append(", tags=").append(tags);
        sb.append('}');
        return sb.toString();
    }
}