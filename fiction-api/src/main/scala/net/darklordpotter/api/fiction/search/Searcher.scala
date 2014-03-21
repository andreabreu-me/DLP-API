package net.darklordpotter.api.fiction.search

import com.twitter.util.Future
import net.darklordpotter.api.fiction.core.{SearchResult, SearchQuery, StoryHeader}

/**
 * 2014-03-19
 * @author Michael Rose
 */

trait Searcher {
  def fetch(ids: Seq[String]):Future[Iterable[StoryHeader]]
  def query(query: SearchQuery, from: Long, max: Long):Future[SearchResult]
}