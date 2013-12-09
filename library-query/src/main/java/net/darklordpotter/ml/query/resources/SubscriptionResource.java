package net.darklordpotter.ml.query.resources;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.yammer.dropwizard.auth.Auth;
import net.darklordpotter.ml.query.api.User;
import net.darklordpotter.ml.query.api.subscriptions.ThreadSubscription;
import net.darklordpotter.ml.query.jdbi.SubscriptionDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * 2013-06-24
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
@Path("/v1/subscriptions")
@Produces("application/json; charset=utf-8")
public class SubscriptionResource {
    private final SubscriptionDAO dao;

    public SubscriptionResource(SubscriptionDAO dao) {
        this.dao = dao;
    }

    @GET
    @Path("/threads")
    public List<ThreadSubscription> subscribedThreads(@Auth User user) {
        return null;
//        return dao.getThreadSubscriptionsForUser(user.getUserId());
    }
}
