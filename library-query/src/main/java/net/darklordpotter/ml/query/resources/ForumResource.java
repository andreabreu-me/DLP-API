package net.darklordpotter.ml.query.resources;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.yammer.dropwizard.auth.Auth;
import net.darklordpotter.ml.query.api.DiscussionThread;
import net.darklordpotter.ml.query.api.Forum;
import net.darklordpotter.ml.query.api.User;
import net.darklordpotter.ml.query.jdbi.ForumDAO;
import net.darklordpotter.ml.query.jdbi.ThreadDAO;

import javax.ws.rs.*;

/**
 * 2013-06-15
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
@Path("/v1/forums")
@Api(value = "/v1/forums", description = "Lists forums and threads in a forum")
@Produces("application/json; charset=utf-8")
public class ForumResource {
    private final ForumDAO forumDAO;
    private final ThreadDAO threadDAO;

    public ForumResource(ForumDAO forumDAO, ThreadDAO threadDAO) {
        this.forumDAO = forumDAO;
        this.threadDAO = threadDAO;
    }

    @GET
    @ApiOperation("list forums")
    public Iterable<Forum> listVisibleForums(@Auth(required = false) User user) {
        System.out.println(user);
        return Iterables.filter(forumDAO.listForums(), new Predicate<Forum>() {
            @Override
            public boolean apply(Forum input) {
                return input.getDepth() <= 2;
            }
        });
    }

    @GET
    @ApiOperation("list threads")
    @Path("/{forumid}/threads")
    public Iterable<DiscussionThread> threadsForForum(@PathParam("forumid") Long forumId,
                                                      @QueryParam("start") @DefaultValue("0") Integer start,
                                                      @QueryParam("limit") @DefaultValue("100") Integer limit) {
        return threadDAO.threadsForForum(forumId, start, limit);
    }
}
