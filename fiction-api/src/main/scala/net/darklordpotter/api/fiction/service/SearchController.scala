//package net.darklordpotter.api.fiction.service
//
//import com.twitter.finatra.Controller
//import org.elasticsearch.client.Client
//import net.darklordpotter.api.fiction.search.{Searcher, ScryerSearcher}
//import net.darklordpotter.api.fiction.core.{StoryToThreadDataManager, ThreadLinker, SearchQuery}
//import com.fasterxml.jackson.datatype.joda.JodaModule
//import com.twitter.finatra.serialization.{JsonSerializer, JacksonJsonSerializer}
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.fasterxml.jackson.module.scala.DefaultScalaModule
//
///**
// * 2014-03-19
// * @author Michael Rose
// */
//class SearchController(client: Client) extends Controller {
//  val mapper = new ObjectMapper().registerModule(new DefaultScalaModule).registerModule(new JodaModule)
//  override val serializer = new JacksonJsonSerializer(mapper)
//
//  val threadLinker = new ThreadLinker(StoryToThreadDataManager.threadLinkSupplier())
//  val searcher:Searcher = new ScryerSearcher(client, threadLinker)
//
//  get("/v1/search/:id") { request =>
//    val ids = request.routeParams.getOrElse("id", "").split(",").map(_.trim).toList
//
//    searcher.fetch(ids).map { result =>
//      render.json(result)
//    }
//  }
//
//  post("/v1/search") { request =>
//    val queryEntity = mapper.readValue(request.request.getInputStream(), classOf[SearchQuery])
//    val from        = request.params.getLongOrElse("from", 0)
//    val max         = request.params.getLongOrElse("max", 25)
//
//    searcher.query(queryEntity, from, max).map { result =>
//      render.json(result)
//    }
//  }
//
//  options("/*") { request =>
//    render
//      .header("Access-Control-Allow-Methods", "GET,POST")
//      .header("Access-Control-Allow-Credentials", "true")
//      .header("Access-Control-Allow-Headers", "X-Requested-With,Content-Type")
//      .header("Access-Control-Max-Age", "171000")
//      .nothing
//      .toFuture
//  }
//}