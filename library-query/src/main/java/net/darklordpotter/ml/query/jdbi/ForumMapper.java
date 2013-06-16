package net.darklordpotter.ml.query.jdbi;

import net.darklordpotter.ml.query.api.Forum;
import org.skife.jdbi.v2.BeanMapper;

/**
 * 2013-06-15
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
public class ForumMapper extends BeanMapper<Forum> {
    public ForumMapper() {
        super(Forum.class);
    }
}
