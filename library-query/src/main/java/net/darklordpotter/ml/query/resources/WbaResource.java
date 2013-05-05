package net.darklordpotter.ml.query.resources;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.yammer.metrics.annotation.Metered;
import net.darklordpotter.ml.query.api.Post;
import net.darklordpotter.ml.query.jdbi.PostDAO;

import javax.annotation.Nullable;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * 2013-04-02
 *
 * @author Michael Rose <elementation@gmail.com>
 */
@Produces("application/json; charset=utf8")
@Path("/wba")
public class WbaResource {
    private final PostDAO dao;

    public WbaResource(final PostDAO dao) {
        this.dao = dao;
    }

    @GET
    @Metered
    @Path("{threadId}")
    public Iterable<Post> getStoryPosts(@PathParam("threadId") Long threadId) {
        List<Post> unfilteredPosts = dao.getWorkByAuthorPosts(threadId);

        return Iterables.filter(unfilteredPosts, new Predicate<Post>() {
            public boolean apply(@Nullable Post input) {
                return input != null && !input.getPageText().startsWith("[QUOTE");
            }

        });
    }

    @GET
    @Metered
    @Path("{threadId}/{postIdx}")
    public Post getStoryPost(@PathParam("threadId") Long threadId, @PathParam("postIdx") Integer postIdx) {
        List<Post> storyPosts = Lists.newArrayList(getStoryPosts(threadId));

        if (postIdx < 1 || postIdx > storyPosts.size()) throw new WebApplicationException(Response.Status.BAD_REQUEST);

        return storyPosts.get(postIdx - 1);
    }
}
