package net.darklordpotter.ml.query.api;

/**
 * 2013-06-15
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
public class Forum {
    private long forumId;
    private String title;
    private String description;
    private int replyCount;
    private long lastPost;
    private String lastPoster;
    private String lastThread;
    private int lastThreadId;
    private int threadCount;
    private String parentlist;

    public long getForumId() {
        return forumId;
    }

    public void setForumId(long forumId) {
        this.forumId = forumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public long getLastPost() {
        return lastPost;
    }

    public void setLastPost(long lastPost) {
        this.lastPost = lastPost;
    }

    public String getLastPoster() {
        return lastPoster;
    }

    public void setLastPoster(String lastPoster) {
        this.lastPoster = lastPoster;
    }

    public String getLastThread() {
        return lastThread;
    }

    public void setLastThread(String lastThread) {
        this.lastThread = lastThread;
    }

    public int getLastThreadId() {
        return lastThreadId;
    }

    public void setLastThreadId(int lastThreadId) {
        this.lastThreadId = lastThreadId;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public String getParentlist() {
        return this.parentlist;
    }

    public long getDepth() {
        if (this.parentlist != null)  {
            int oldLen = this.parentlist.length();
            return oldLen - this.parentlist.replace(",", "").length();
        }

        return 0;
    }

    public void setParentlist(String parentlist) {
        this.parentlist = parentlist;
    }
}
