package net.darklordpotter.ml.new_extraction.filters

import net.darklordpotter.ml.new_extraction.ExtractionAction
import net.darklordpotter.ml.new_extraction.LibraryExtractor.ExtractionContext

/**
 * 2013-08-19
 * @author Michael Rose
 */
class BBCodeFilter extends ExtractionAction {
  val bbcode = "\\[[^\\]]+\\]".r

  def apply(ctx: ExtractionContext) {
    ctx.story.pagetext = bbcode.replaceAllIn(ctx.story.pagetext, "")
  }
}