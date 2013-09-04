package net.darklordpotter.ml.query.jdbi;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

import java.util.List;

/**
 * 2013-08-21
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
public interface SimilarityDAO {
    @SqlQuery("SELECT threadId2 FROM thread_similarity WHERE threadId = :threadId ORDER BY jaccardSimilarity DESC LIMIT 5")
    public List<Long> similarThreads(@Bind("threadId") Long threadId);
}
