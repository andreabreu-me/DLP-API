package net.darklordpotter.api.fiction

import javax.ws.rs._
import net.darklordpotter.api.fiction.core.{StoryHeader, SearchResult, SearchQuery}
import net.darklordpotter.api.fiction.search.Searcher
import org.slf4j.LoggerFactory
//import net.darklordpotter.ml.query.Constants

//import net.darklordpotter.ml.query.core.DateParam

/**
 * 2014-04-16
 * @author Michael Rose <michael@fullcontact.com>
 */
@Path("/v1/search")
@Produces(Array("application/json"))
class SearchResource(searcher: Searcher) {
  val log = LoggerFactory.getLogger(this.getClass)

  @GET
  @Path("/{id: [0-9,]+}") def getById(@PathParam("id") id: String): Iterable[StoryHeader] = {
    searcher.fetch(id.split(",").map(_.trim))
      .toBlockingObservable
      .toIterable
  }

//  @GET
//  @Path("/updates") def updatesSince(@QueryParam("since") dateParam: DateParam,
//                                     @QueryParam("from") @DefaultValue("0") from: Int,
//                                     @QueryParam("max") @DefaultValue("50") max: Int): SearchResult = {
//    if (max > 250) max = 250
//    return searcher.updatesSince(Option.apply(dateParam.get), from, max).asJavaObservable.toBlockingObservable.single
//  }

  //  @ApiOperation(value = "Search the story index for a given SearchQuery", response = classOf[SearchResult])
  @POST
  @Path("/") def query(query: SearchQuery,
                       @QueryParam("from") @DefaultValue("0") from: Int,
                       @QueryParam("max") @DefaultValue("50") max: Int): SearchResult = {
//    if (max > 250) max = 250

    val result: SearchResult = searcher.query(query, from, max)
      .toBlockingObservable
      .single

    log.info("Query: {}", query)
//    log.info("Query Results: {} hits, {} max score, completed in {} ms", result.getHits, result.getMaxScore, result.getTook)

    result
  }
}