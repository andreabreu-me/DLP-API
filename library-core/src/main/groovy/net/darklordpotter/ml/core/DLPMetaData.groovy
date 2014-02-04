package net.darklordpotter.ml.core

import org.joda.time.DateTime

/**
 * 2014-02-03
 * @author Michael Rose <michael@fullcontact.com>
 */
public class DLPMetaData {
    int posts;
    int views;
    DateTime posted;
    DateTime lastPost;
    String forum;

    public int getPosts() {
        return posts
    }

    public void setPosts(int posts) {
        this.posts = posts
    }

    public int getViews() {
        return views
    }

    public void setViews(int views) {
        this.views = views
    }

    public DateTime getPosted() {
        return posted
    }

    public void setPosted(DateTime posted) {
        this.posted = posted
    }

    public DateTime getLastPost() {
        return lastPost
    }

    public void setLastPost(DateTime lastPost) {
        this.lastPost = lastPost
    }

    public String getForum() {
        return forum
    }

    public void setForum(String forum) {
        this.forum = forum
    }
}
