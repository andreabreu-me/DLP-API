package net.darklordpotter.ml.query.jdbi;

import net.darklordpotter.ml.query.api.DiscussionThread;
import net.darklordpotter.ml.query.api.Forum;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

/**
 * 2013-06-15
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
@RegisterMapper(ForumMapper.class)
public interface ForumDAO {
    @SqlQuery("SELECT * FROM forum WHERE forumid NOT IN (7,21, 98, 51, 138, 77)")
    public List<Forum> listForums();

    public List<DiscussionThread> threadsForForum(Long forumId);
}
