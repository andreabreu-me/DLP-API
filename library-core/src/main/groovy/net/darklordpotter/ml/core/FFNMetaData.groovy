package net.darklordpotter.ml.core

import org.joda.time.DateTime

/**
 * 2014-02-03
 * @author Michael Rose <michael@fullcontact.com>
 */
public class FFNMetaData {
    int reviews;
    int favorites;
    int follows;
    int words;
    int chapters;
    String status = "";
    DateTime published;
    DateTime updated;

    public int getReviews() {
        return reviews
    }

    public void setReviews(int reviews) {
        this.reviews = reviews
    }

    public int getFavorites() {
        return favorites
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites
    }

    public int getFollows() {
        return follows
    }

    public void setFollows(int follows) {
        this.follows = follows
    }

    public int getWords() {
        return words
    }

    public void setWords(int words) {
        this.words = words
    }

    public int getChapters() {
        return chapters
    }

    public void setChapters(int chapters) {
        this.chapters = chapters
    }

    public String getStatus() {
        return status
    }

    public void setStatus(String status) {
        this.status = status
    }

    public DateTime getPublished() {
        return published
    }

    public void setPublished(DateTime published) {
        this.published = published
    }

    public DateTime getUpdated() {
        return updated
    }

    public void setUpdated(DateTime updated) {
        this.updated = updated
    }
}
