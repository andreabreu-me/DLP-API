package net.darklordpotter.ml.query.jdbi;

import net.darklordpotter.ml.query.api.subscriptions.ThreadSubscription;
import org.skife.jdbi.v2.BeanMapper;

/**
 * 2013-06-24
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
public class ThreadSubscriptionMapper extends BeanMapper<ThreadSubscription> {
    public ThreadSubscriptionMapper() {
        super(ThreadSubscription.class);
    }
}
