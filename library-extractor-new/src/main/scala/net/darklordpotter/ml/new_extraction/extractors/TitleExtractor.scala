package net.darklordpotter.ml.new_extraction.extractors

import net.darklordpotter.ml.new_extraction.LibraryExtractor.ExtractionContext
import net.darklordpotter.ml.new_extraction.models.{Author, Title, Rating}
import net.darklordpotter.ml.new_extraction.ExtractionAction

/**
 * 2013-08-14
 * @author Michael Rose <michael@fullcontact.com>
 */
class TitleExtractor extends ExtractionAction {
  val regex = "Title:(.*)".r
  val threadTitle = "(.*) by (.*) - ([a-zA-Z\\-+0-9]+)".r

  def apply(ctx: ExtractionContext) {
    ctx.elements ++= applyRegex(regex, ctx.story.pagetext).map(Title)
    println (applyRegexAll(threadTitle,ctx.story.title) match {
      case List(title, _*) => List(Title(title))
      case _ => List()
    })
  }
 }