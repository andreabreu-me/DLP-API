package net.darklordpotter.ml.query.jdbi;

import net.darklordpotter.ml.query.api.Post;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

/**
 * 2013-03-27
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
@RegisterMapper(PostMapper.class)
public interface PostDAO {
    @SqlQuery("SELECT p.* FROM post p \n" +
            "LEFT JOIN thread t \n" +
            "USING (threadid) \n" +
            "WHERE \n" +
            "t.threadid=:threadId AND\n" +
            "(LENGTH(p.pagetext) > 4000 OR t.firstpostid = p.postid) AND\n" +
            "p.userid = t.postuserid AND\n" +
            "t.forumid IN (7,11, 72, 67, 96, 77)\n" +
            "ORDER BY postid ASC")
    public List<Post> getWorkByAuthorPosts(@Bind("threadId") Long threadId);

    @SqlQuery("SELECT p.*,u.avatarRevision,u.usertitle,t.title as threadTitle FROM post p \n" +
            "STRAIGHT_JOIN thread t\n" +
            "ON t.threadid = p.threadid\n" +
            "LEFT JOIN user u\n" +
            "USING (userid)\n" +
            "WHERE\n" +
            "t.forumid NOT IN (7,21, 98, 51, 138, 77)\n" +
            "ORDER BY postid DESC\n" +
            "LIMIT :skip,:limit")
    public List<Post> getLatestPosts(@Bind("skip") Integer skip, @Bind("limit") Integer limit);

    @SqlQuery("SELECT p.*,u.avatarRevision,u.usertitle,t.title as threadTitle FROM post p \n" +
            "STRAIGHT_JOIN thread t\n" +
            "ON t.threadid = p.threadid\n" +
            "LEFT JOIN user u\n" +
            "USING (userid)\n" +
            "WHERE\n" +
            "t.forumid NOT IN (7,21, 98, 51, 138, 77)\n" +
            "p.post_id > lastId" +
            "ORDER BY postid DESC\n" +
            "LIMIT :limit")
    public List<Post> getLatestPostsAfterId(@Bind("lastId") Long lastId, @Bind("limit") Integer limit);

    @SqlQuery("SELECT p.*,u.avatarRevision,u.usertitle,t.title as threadTitle FROM post p \n" +
            "STRAIGHT_JOIN thread t\n" +
            "ON t.threadid = p.threadid\n" +
            "LEFT JOIN user u\n" +
            "USING (userid)\n" +
            "WHERE\n" +
            "t.threadid = :threadId\n" +
            "ORDER BY postid ASC\n" +
            "LIMIT :limit")
    public List<Post> getPostsForThreadId(@Bind("threadId") Long threadId, @Bind("limit") Integer limit);
}
