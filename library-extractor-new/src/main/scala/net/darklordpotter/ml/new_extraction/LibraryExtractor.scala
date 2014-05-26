package net.darklordpotter.ml.new_extraction

import scala.collection.mutable
import net.darklordpotter.ml.new_extraction.models._
import net.darklordpotter.ml.extraction.providers.DLPDataProvider
import scala.collection.JavaConverters._
import net.darklordpotter.ml.new_extraction.extractors._
import net.darklordpotter.ml.new_extraction.filters.{UrlDecoderFilter, BBCodeFilter}
import net.darklordpotter.ml.new_extraction.models.Summary
import net.darklordpotter.ml.new_extraction.models.Author
import net.darklordpotter.ml.new_extraction.models.Title

/**
 * 2013-08-14
 * @author Michael Rose
 */
object LibraryExtractor extends App {
  case class ExtractionContext(story: StoryRow, elements: mutable.Set[StoryElement])

//  val stories = Http("https://forums.darklordpotter.net/data/library_latest.tsv").asString.split("\n").map(StoryRow.fromLine).drop(1).toList
  val stories = new DLPDataProvider("localhost", "darklord_mainvb", "root", "").getData.asScala.toList.map(StoryRow.fromMap)

  val pipeline = new ExtractionPipeline()
    .register(new UrlDecoderFilter)
    .register(new BBCodeFilter)
    .register(new AuthorExtractor)
    .register(new TitleExtractor)
    .register(new RatingExtractor)
    .register(new UrlExtractor)
    .register(new SummaryExtractor)

  val extracted = stories.map { story => pipeline.execute(ExtractionContext(story, mutable.HashSet())) }.toList

  var authors = 0
  var titles = 0
  var summaries = 0
  var ratings = 0
  var urls = 0

  extracted.foreach { story =>
    if (story.elements.exists(_.isInstanceOf[Title])) titles += 1
    if (story.elements.exists(_.isInstanceOf[Author])) authors += 1
    if (story.elements.exists(_.isInstanceOf[Summary])) summaries += 1
    if (story.elements.exists(_.isInstanceOf[Url])) urls += 1
    if (story.elements.exists(_.isInstanceOf[Rating])) ratings += 1
  }

  println(s"authors=${authors}/662, titles=${titles}/681, summaries=${summaries}/638, ratings=${ratings}/618, urls=${urls}/566, total=${extracted.size}")

  // to beat: StatContext{authors=662, titles=681, summaries=638, ratings=618, urls=566, total=681}

  println(s"${extracted.count(_.elements.size == 0)}/${extracted.size}")
  println(s"${extracted.count(_.elements.size == 1)}/${extracted.size}")
  println(s"${extracted.count(_.elements.size == 2)}/${extracted.size}")
  println(s"${extracted.count(_.elements.size == 3)}/${extracted.size}")
  println(s"${extracted.count(_.elements.size == 4)}/${extracted.size}")
  println(s"${extracted.count(_.elements.size == 5)}/${extracted.size}")
  println(s"${extracted.count(_.elements.size == 6)}/${extracted.size}")
  println(s"${extracted.count(_.elements.size == 7)}/${extracted.size}")
}
