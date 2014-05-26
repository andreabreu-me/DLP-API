package net.darklordpotter.api.fiction.core

import org.joda.time.DateTime

/**
 * 2014-03-23
 * @author Michael Rose <michael@fullcontact.com>
 */

/*
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

 */

case class StoryCover(score: Double,
                       title: String,
                       author: String,
                       authorUrl: String,
                       authorId: Long,
                       summary: String,
                       url: String,
                       urlLatest: String,
                       published: DateTime,
                       updated: DateTime,
                       meta: StoryMeta) {

}