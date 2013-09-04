package net.darklordpotter.ml.query.jdbi;

import net.darklordpotter.ml.query.api.subscriptions.ThreadSubscription;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

/**
 * 2013-06-24
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
@RegisterMapper(ThreadSubscriptionMapper.class)
public interface SubscriptionDAO {
    @SqlQuery("SELECT * FROM subscribethread WHERE userid = :userId")
    List<ThreadSubscription> getThreadSubscriptionsForUser(@Bind("userId") long userId);
}
