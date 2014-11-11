package net.darklordpotter.ml.query.resources;

import com.google.common.collect.Lists;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.yammer.metrics.annotation.Metered;
import net.darklordpotter.ml.query.Constants;
import net.darklordpotter.ml.query.api.ffdb.Character;
import net.darklordpotter.ml.query.api.ffdb.Fandom;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.util.List;

/**
 * 2013-12-23
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
@Api(value = "/v1/fandoms", description = "Characters API with character lists for fandoms")
@Path("/v1/fandoms")
@Produces(Constants.APPLICATION_JSON)
public class FandomResource {
    private final Client client;
    private Logger log = LoggerFactory.getLogger(FandomResource.class);

    public FandomResource(Client client) {
        this.client = client;
    }

    @ApiOperation("Gets fandoms")
    @GET
    @Metered
    public Iterable<Fandom> getFandoms() throws IOException {
        SearchRequestBuilder search = client.prepareSearch("ffncrossover_index")
                .setTypes("fandom")
                .setSize(10000);

        search.addSort("name.raw", SortOrder.ASC);

        // TODO: Add count to each fandom

        SearchResponse response = search.get();

        List<Fandom> fandoms = Lists.newArrayList();
        for (SearchHit hit : response.getHits().getHits()) {
            fandoms.add(Fandom.fromEs(hit.getSource()));
        }

        return fandoms;
    }
}
