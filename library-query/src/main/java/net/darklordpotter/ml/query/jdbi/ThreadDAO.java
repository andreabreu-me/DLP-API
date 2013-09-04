package net.darklordpotter.ml.query.jdbi;

import net.darklordpotter.ml.query.api.DiscussionThread;
import net.darklordpotter.ml.query.api.Forum;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

/**
 * 2013-06-15
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
@RegisterMapper(DiscussionThreadMapper.class)
public interface ThreadDAO {
    @SqlQuery("SELECT * FROM thread WHERE visible > 0 AND forumid = :forumId ORDER BY lastpost DESC")
    public List<DiscussionThread> threadsForForum(@Bind("forumId") Long forumId);
}
