package net.darklordpotter.ml.query.resources;

import net.darklordpotter.ml.query.api.UserCredential;
import net.darklordpotter.ml.query.jdbi.AuthDAO;

import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

/**
 * 2013-06-12
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
@Path("/v1/auth")
@Produces("application/json; charset=utf-8")
public class AuthResource {
    AuthDAO authDAO;

    public AuthResource(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    @POST
    public String login(@Valid UserCredential userCredential) {
        String authKey = authDAO.authUser(userCredential.getUsername(), userCredential.getMd5password());

        if (authKey == null) throw new WebApplicationException(403);

        return authKey;
    }
}
