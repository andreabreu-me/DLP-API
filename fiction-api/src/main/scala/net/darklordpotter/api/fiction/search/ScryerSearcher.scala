package net.darklordpotter.api.fiction.search

import net.darklordpotter.api.fiction.core._
import org.elasticsearch.client.Client
import org.elasticsearch.action.search.SearchType
import ElasticSearch._
import scala.collection.JavaConverters._
import rx.lang.scala._
import org.joda.time.DateTime

class ScryerSearcher(client: Client, threadLinker: ThreadLinker) extends Searcher {
  override def fetch(ids: Seq[String]):Observable[StoryHeader] = {
    val esFuture = client
      .prepareMultiGet()
      .add("ffn_index", "story", ids.asJava)
      .execute()
      .toObservable

    esFuture
      // Obs[Seq[T]] -> N*Obs[T] -> flatten
      .flatMap { multiGetResponse => Observable.from(multiGetResponse.getResponses.map(_.getResponse.getSource)) }
      .map(StoryHeader.fromSource)
  }

  override def updatesSince(date: Option[DateTime], from: Long, max: Long) = ???

  override def query(query: SearchQuery, from: Long, max: Long):Observable[SearchResult] =  {
    val fictionQueryBuilder = new FictionSearchBuilder(query, threadLinker)

    val searchBuilder = client.prepareSearch("ffn_index")
      .setTypes("story")
      .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
      .setQuery(fictionQueryBuilder.toQueryBuilder)
      .setFrom(from.toInt)
      .setSize(max.toInt)

    if (query.getSortBy.toLowerCase == "_popular") {
      searchBuilder.setSearchType(SearchType.QUERY_THEN_FETCH)
    }

    fictionQueryBuilder.addSort(searchBuilder)

    searchBuilder.execute().toObservable.map {
      SearchResult.fromResult(_, threadLinker)
    }
  }

}