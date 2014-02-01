package net.darklordpotter.ml.new_extraction.extractors

import net.darklordpotter.ml.new_extraction.LibraryExtractor.ExtractionContext
import net.darklordpotter.ml.new_extraction.models.{Summary, Title}
import net.darklordpotter.ml.new_extraction.ExtractionAction

/**
 * 2013-08-14
 * @author Michael Rose <michael@fullcontact.com>
 */
class SummaryExtractor extends ExtractionAction {
  val regex = "Summary:(.*)".r

  def apply(ctx: ExtractionContext) {
    ctx.elements ++= applyRegex(regex, ctx.story.pagetext).map(Summary)
  }
 }