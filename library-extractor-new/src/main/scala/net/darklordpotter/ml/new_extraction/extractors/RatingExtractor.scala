package net.darklordpotter.ml.new_extraction.extractors

import net.darklordpotter.ml.new_extraction.LibraryExtractor.ExtractionContext
import net.darklordpotter.ml.new_extraction.models.Rating
import net.darklordpotter.ml.new_extraction.ExtractionAction

/**
 * 2013-08-14
 * @author Michael Rose <michael@fullcontact.com>
 */
class RatingExtractor extends ExtractionAction {
  val regex = "Rating:\\s?([a-zA-Z\\-+0-9]+)".r
  val titleRegex = "(.*) by (.*) - ([a-zA-Z\\-+0-9]+)".r

  def apply(ctx: ExtractionContext) {
    ctx.elements ++= applyRegex(regex, ctx.story.pagetext).filter(acceptableRating).map(Rating)
    ctx.elements ++= (applyRegexAll(titleRegex, ctx.story.title).filter(acceptableRating) match {
      case List(_, _, rating) => List(Rating(rating))
      case _ => List()
    })
  }

  val valid = List("G", "PG", "PG13", "R", "NC17", "K", "K+", "T", "M", "MA")
  def acceptableRating(str:String):Boolean = valid.contains(str.toUpperCase.replaceAll("[^A-Z0-9+]+", ""))
}