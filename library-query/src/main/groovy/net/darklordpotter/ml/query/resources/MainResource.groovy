package net.darklordpotter.ml.query.resources

import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * 2013-02-25
 * @author Michael Rose <michael@fullcontact.com>
 */
@Path("/")
class MainResource {
    @Produces(MediaType.APPLICATION_XHTML_XML)
    public Response getIndex() {
        Response.ok().entity("OK").build()
    }
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIndexJson() {
        Response.ok().entity([:]).build()
    }
}
