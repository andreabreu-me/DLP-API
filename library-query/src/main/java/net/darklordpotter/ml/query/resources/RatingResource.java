package net.darklordpotter.ml.query.resources;

import com.yammer.dropwizard.auth.Auth;
import net.darklordpotter.ml.query.api.ThreadRating;
import net.darklordpotter.ml.query.api.User;
import net.darklordpotter.ml.query.jdbi.ThreadRatingDao;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 2013-11-29
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
@Produces(MediaType.APPLICATION_JSON)
@Path("/v1/ratings")
public class RatingResource {
    private final ThreadRatingDao threadRatingDao;

    public RatingResource(ThreadRatingDao threadRatingDao) {
        this.threadRatingDao = threadRatingDao;
    }

    @GET
    @Path("/")
    public Iterable<ThreadRating> getRatingsByUser(@Auth(required = true) User user) {
        return threadRatingDao.getRatingsForUser(user.getUserId());
    }
//    @GET
//    @Path("/{userId}")
//    public Iterable<ThreadRating> getRatingsByUser(@PathParam("userId") Long userId) {
//        return threadRatingDao.getRatingsForUser(userId);
//    }
}
