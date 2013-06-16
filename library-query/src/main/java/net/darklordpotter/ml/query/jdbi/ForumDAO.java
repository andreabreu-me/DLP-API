package net.darklordpotter.ml.query.jdbi;

import net.darklordpotter.ml.query.api.Forum;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

/**
 * 2013-06-15
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
@RegisterMapper(ForumMapper.class)
public interface ForumDAO {
    @SqlQuery("SELECT * FROM forum")
    public List<Forum> listForums();
}
