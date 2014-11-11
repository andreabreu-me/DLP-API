package net.darklordpotter.ml.query.resources;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.yammer.metrics.annotation.Metered;
import net.darklordpotter.ml.query.Constants;
import net.darklordpotter.ml.query.api.ffdb.Character;
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
import java.util.Collections;
import java.util.List;

/**
 * 2013-12-23
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
@Api(value = "/v1/characters", description = "Characters API with character lists for fandoms")
@Path("/v1/characters")
@Produces(Constants.APPLICATION_JSON)
public class CharacterResource {
    private final Client client;
    private final Splitter commaSplitter = Splitter.on(',').trimResults().omitEmptyStrings();
    private Logger log = LoggerFactory.getLogger(CharacterResource.class);

    public CharacterResource(Client client) {
        this.client = client;
    }

    @ApiOperation("Gets all characters")
    @GET
    @Metered
    public Iterable<Character> getAllCharacters() throws IOException {
        SearchRequestBuilder search = client.prepareSearch("ffncrossover_index").setTypes("character").setSize(10000);

        search.addSort("name.raw", SortOrder.ASC);

        return convertToCharacters(search.get());
    }

    @ApiOperation("Gets characters for a fandom")
    @GET
    @Path("/{fandomIds}")
    @Metered
    public Iterable<Character> getCharactersForFandoms(@PathParam("fandomIds") String fandomIds) throws IOException {
        List<String> ids;
        if (fandomIds.contains(",")) {
            ids = Lists.newArrayList(commaSplitter.split(fandomIds));
        } else {
            ids = Collections.singletonList(fandomIds);
        }

        SearchRequestBuilder search = client.prepareSearch("ffncrossover_index")
                .setTypes("character")
                .setSize(1000);

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(
                QueryBuilders.inQuery("fandom_id", ids)
        );

        search.setQuery(queryBuilder);
        search.addSort("name.raw", SortOrder.ASC);

        SearchResponse response = search.get();

        return convertToCharacters(response);
    }

    private List<Character> convertToCharacters(SearchResponse response) {
        List<Character> characters = Lists.newArrayList();
        for (SearchHit hit : response.getHits().getHits()) {
            characters.add(Character.fromEs(hit.getSource()));
        }
        return characters;
    }
}
