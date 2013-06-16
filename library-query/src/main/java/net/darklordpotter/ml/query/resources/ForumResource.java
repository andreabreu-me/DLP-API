package net.darklordpotter.ml.query.resources;

import net.darklordpotter.ml.query.api.Forum;
import net.darklordpotter.ml.query.jdbi.ForumDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * 2013-06-15
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
@Path("/v1/forums")
@Produces("application/json; charset=utf-8")
public class ForumResource {
    private final ForumDAO forumDAO;

    public ForumResource(ForumDAO forumDAO) {
        this.forumDAO = forumDAO;
    }

    @GET
    public List<Forum> listForums() {
        return forumDAO.listForums();
    }
}
