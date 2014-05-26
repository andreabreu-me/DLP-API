package net.darklordpotter.api.fiction.search

import net.darklordpotter.api.fiction.core.{SearchResult, SearchQuery, StoryHeader}
import rx.lang.scala.Observable
import org.joda.time.DateTime

/**
 * 2014-03-19
 * @author Michael Rose
 */

trait Searcher {
  def fetch(ids: Seq[String]):Observable[StoryHeader]
  def updatesSince(date: Option[DateTime], from: Long, max: Long):Observable[SearchResult]
  def query(query: SearchQuery, from: Long, max: Long):Observable[SearchResult]
}