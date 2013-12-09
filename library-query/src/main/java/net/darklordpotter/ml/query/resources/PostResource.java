package net.darklordpotter.ml.query.resources;

import net.darklordpotter.ml.query.api.Post;
import net.darklordpotter.ml.query.jdbi.PostDAO;

import javax.ws.rs.*;
import java.util.List;

/**
 * 2013-06-06
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
@Path("/v1/posts")
@Produces("application/json; charset=utf-8")
public class PostResource {
    private final PostDAO dao;

    public PostResource(final PostDAO dao) {
        this.dao = dao;
    }

    @GET
    @Path("latest")
    public List<Post> latestPosts(@QueryParam("start") @DefaultValue("0") Integer start,
                                  @QueryParam("limit") @DefaultValue("50") Integer limit) {
        return dao.getLatestPosts(start, limit);
    }

    @GET
    @Path("thread/{threadId}")
    public List<Post> postForThreadId(@PathParam("threadId") Long threadId,
                                      @QueryParam("start") @DefaultValue("0") Integer start,
                                      @QueryParam("limit") @DefaultValue("5000") Integer limit) {
        return dao.getPostsForThreadId(threadId, start, limit);
    }
}
