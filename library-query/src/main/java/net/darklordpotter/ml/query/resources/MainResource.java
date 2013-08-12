package net.darklordpotter.ml.query.resources;

import com.google.common.collect.Maps;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedHashMap;

/**
 * 2013-02-25
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
@Path("/")
public class MainResource {
    @Produces(MediaType.APPLICATION_XHTML_XML)
    public Response getIndex() {
        return Response.ok().entity("OK").build();
    }

    @Produces(MediaType.APPLICATION_JSON)
    public Response getIndexJson() {
        return Response.ok().entity(Maps.newHashMap()).build();
    }

}
