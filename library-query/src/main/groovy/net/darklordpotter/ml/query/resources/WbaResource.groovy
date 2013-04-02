package net.darklordpotter.ml.query.resources

import com.mongodb.DBCollection
import com.yammer.metrics.annotation.Metered
import net.darklordpotter.ml.core.Story
import net.darklordpotter.ml.query.api.Post
import net.darklordpotter.ml.query.jdbi.PostDAO
import net.vz.mongodb.jackson.JacksonDBCollection

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response

/**
 * 2013-04-02
 * @author Michael Rose <elementation@gmail.com>
 */
@Produces("application/json; charset=utf8")
@Path("/wba")
class WbaResource {
    private final PostDAO dao

    WbaResource(final PostDAO dao) {
        this.dao = dao
    }


    @GET
    @Metered
    @Path("{threadId}")
    List<Post> getStoryPosts(@PathParam("threadId") Long threadId) {
        dao.getWorkByAuthorPosts(threadId).findAll() {
            !it.pageText.startsWith("[QUOTE")
        }
    }


    @GET
    @Metered
    @Path("{threadId}/{postIdx}")
    Post getStoryPost(@PathParam("threadId") Long threadId, @PathParam("postIdx") Integer postIdx) {
        List<Post> storyPosts = getStoryPosts(threadId)

        if (postIdx < 1 || postIdx > storyPosts.size()) throw new WebApplicationException(Response.Status.BAD_REQUEST)

        return storyPosts.get(postIdx - 1)
    }

}
