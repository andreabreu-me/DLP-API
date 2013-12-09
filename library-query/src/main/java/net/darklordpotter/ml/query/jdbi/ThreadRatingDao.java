package net.darklordpotter.ml.query.jdbi;

/**
 * 2013-11-29
 *
 * @author Michael Rose <michael@fullcontact.com>
 */

import com.google.common.collect.ImmutableList;
import net.darklordpotter.ml.query.api.ThreadRating;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import org.skife.jdbi.v2.tweak.BeanMapperFactory;

import java.util.List;

@RegisterMapperFactory(BeanMapperFactory.class)
public interface ThreadRatingDao {
    @SqlQuery("SELECT * FROM threadrate WHERE userid = :userId")
    public List<ThreadRating> getRatingsForUser(@Bind("userId") Long userId);
}
