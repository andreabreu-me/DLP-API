package net.darklordpotter.ml.query.api;

/**
 * 2013-06-15
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
public class DiscussionThread {
    private long threadId;
    private String title;
    private String prefixId;

    private long replyCount;
    private String postUsername;

    private long postUserId;
    private String lastPoster;
    private long lastPost;

    private long dateline;
    private long firstPostId;

    private long lastPostId;

    private long views;

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrefixId() {
        return prefixId;
    }

    public void setPrefixId(String prefixId) {
        this.prefixId = prefixId;
    }

    public long getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(long replyCount) {
        this.replyCount = replyCount;
    }

    public String getPostUsername() {
        return postUsername;
    }

    public void setPostUsername(String postUsername) {
        this.postUsername = postUsername;
    }

    public long getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(long postUserId) {
        this.postUserId = postUserId;
    }

    public String getLastPoster() {
        return lastPoster;
    }

    public void setLastPoster(String lastPoster) {
        this.lastPoster = lastPoster;
    }

    public long getLastPost() {
        return lastPost;
    }

    public void setLastPost(long lastPost) {
        this.lastPost = lastPost;
    }

    public long getDateline() {
        return dateline;
    }

    public void setDateline(long dateline) {
        this.dateline = dateline;
    }

    public long getFirstPostId() {
        return firstPostId;
    }

    public void setFirstPostId(long firstPostId) {
        this.firstPostId = firstPostId;
    }

    public long getLastPostId() {
        return lastPostId;
    }

    public void setLastPostId(long lastPostId) {
        this.lastPostId = lastPostId;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }
}
