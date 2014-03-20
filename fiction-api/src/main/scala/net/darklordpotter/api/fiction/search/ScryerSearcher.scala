package net.darklordpotter.api.fiction.search

import net.darklordpotter.api.fiction.core._
import org.elasticsearch.client.Client
import org.elasticsearch.action.search.SearchType
import ElasticSearch._
import scala.collection.JavaConverters._
import com.twitter.util.Future

class ScryerSearcher(client: Client, threadLinker: ThreadLinker) extends Searcher {
  def fetch(ids: Seq[String]):Future[Iterable[StoryHeader]] = {
    val esFuture = client
      .prepareMultiGet()
      .add("ffn_index", "story", ids.asJava)
      .execute()

    esFuture.toPromise map { multiGetResponse =>
      multiGetResponse.iterator().asScala.map(_.getResponse.getSource).toIterable
    } map { sources =>
      sources.map { source =>
        StoryHeader.fromSource(source)
      }
    }
  }

  def query(query: SearchQuery, from: Long, max: Long):Future[SearchResult] =  {
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

    searchBuilder.execute().toPromise map {
      SearchResult.fromResult(_, threadLinker)
    }
  }
}