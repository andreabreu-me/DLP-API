package net.darklordpotter.ml.query.api;

/**
 * 2013-03-27
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
public class Post {
    Long postId;
    Long threadId;
    String threadTitle;
    String username;
    Long userId;
    String userTitle;
    String title;
    Long dateline;
    String pageText;
    Integer avatarRevision;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public String getThreadTitle() {
        return threadTitle;
    }

    public void setThreadTitle(String threadTitle) {
        this.threadTitle = threadTitle;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getDateline() {
        return dateline;
    }

    public void setDateline(Long dateline) {
        this.dateline = dateline;
    }

    public String getPageText() {
        return pageText;
    }

    public void setPageText(String pageText) {
        this.pageText = pageText;
    }

    public Integer getAvatarRevision() {
        return avatarRevision;
    }

    public void setAvatarRevision(Integer avatarRevision) {
        this.avatarRevision = avatarRevision;
    }
}
