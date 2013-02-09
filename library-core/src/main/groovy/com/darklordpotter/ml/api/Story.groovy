package com.darklordpotter.ml.api

import com.mongodb.BasicDBObject
import com.mongodb.DBObject

import javax.persistence.Id

/**
 * 2013-02-07
 * @author Michael Rose <michael@fullcontact.com>
 */
public class Story {
    private final Long threadId = 0
    String title = ""
    String author = ""
    String summary = ""
    Rating rating
    List<String> url = []

    Story(Long threadId) {
        this.threadId = threadId
    }

    @Id
    public String getMongoId() {
        return threadId.toString()
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Story");
        sb.append("{threadId=").append(threadId);
        sb.append(", title='").append(title).append('\'');
        sb.append(", author='").append(author).append('\'');
        sb.append(", summary='").append(summary).append('\'');
        sb.append(", url=").append(url);
        sb.append('}');
        return sb.toString();
    }
}