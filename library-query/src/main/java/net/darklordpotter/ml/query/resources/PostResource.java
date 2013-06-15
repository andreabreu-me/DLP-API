package net.darklordpotter.ml.query.resources;

import net.darklordpotter.ml.query.api.Post;
import net.darklordpotter.ml.query.jdbi.PostDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * 2013-06-06
 *
 * @author Michael Rose <michael@fullcontact.com>
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
    public List<Post> latestPosts() {
        return dao.getLatestPosts(0, 50);
    }
}
