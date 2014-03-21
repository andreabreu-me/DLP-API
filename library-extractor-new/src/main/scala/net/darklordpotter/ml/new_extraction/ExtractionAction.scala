package net.darklordpotter.ml.new_extraction

import net.darklordpotter.ml.new_extraction.LibraryExtractor.ExtractionContext
import net.darklordpotter.ml.new_extraction.models.StoryElement
import scala.util.matching.Regex

/**
 * 2013-08-14
 * @author Michael Rose
 */
trait ExtractionAction {
  def apply(ctx: ExtractionContext)

  def applyRegex(regex: Regex, text: String): List[String] = {
    regex.findAllMatchIn(text).toList.map(_.group(1).trim)
  }
  def applyRegexAll(regex: Regex, text: String): List[String] = {
    regex.findAllMatchIn(text).toList.flatMap(_.subgroups).map(_.trim)
  }
}