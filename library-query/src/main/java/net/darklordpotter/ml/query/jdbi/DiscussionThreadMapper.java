package net.darklordpotter.ml.query.jdbi;

import net.darklordpotter.ml.query.api.DiscussionThread;
import net.darklordpotter.ml.query.api.Forum;
import org.skife.jdbi.v2.BeanMapper;

/**
 * 2013-06-15
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
public class DiscussionThreadMapper extends BeanMapper<DiscussionThread> {
    public DiscussionThreadMapper() {
        super(DiscussionThread.class);
    }
}
