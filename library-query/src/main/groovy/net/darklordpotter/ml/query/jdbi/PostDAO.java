package net.darklordpotter.ml.query.jdbi;

import net.darklordpotter.ml.query.api.Post;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

/**
 * 2013-03-27
 *
 * @author Michael Rose <michael@fullcontact.com>
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
            "t.forumid IN (11, 72, 67, 96)\n" +
            "ORDER BY postid ASC")
    public List<Post> getWorkByAuthorPosts(@Bind("threadId") Long threadId);
}
