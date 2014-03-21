package net.darklordpotter.ml.new_extraction.extractors

import net.darklordpotter.ml.new_extraction.LibraryExtractor.ExtractionContext
import net.darklordpotter.ml.new_extraction.models.Author
import net.darklordpotter.ml.new_extraction.ExtractionAction
import net.darklordpotter.ml.new_extraction.utils.Levenshtein

/**
 * 2013-08-14
 * @author Michael Rose
 */
class AuthorExtractor extends ExtractionAction {
  val regex = "Author:((.*))".r
  val titleRegex = "by ([^-–]+)".r

  def apply(ctx: ExtractionContext) {
//    regex.findAllMatchIn(ctx.story.pagetext).toList.foreach { x =>
//      ctx.elements += Author(x.group(1).trim)
//    }

    val authorsFromText = applyRegex(regex, ctx.story.pagetext).map(splitMulti).flatten
    val authorsFromTitle = applyRegex(titleRegex, ctx.story.title).map(splitMulti).flatten
//
//    (authorsFromText++authorsFromTitle).combinations(2).map { c =>
//      (c, c.map(filterName).toSet, Levenshtein.distance(filterName(c(0)), filterName(c(1))))
//    }.filter(_._3 > 0).map { m =>
//      (m._1, m._2, m._3, prefixOrSuffixOf(m._1(0),m._1(1)))
//    }.foreach(println)

    ctx.elements ++= authorsFromText.map(Author)
    ctx.elements ++= authorsFromTitle.map(Author)
  }

  def splitMulti(text: String) = {
    text.split('/').toList
  }

  def filterName(text: String) = {
    text
      .toLowerCase
      .replaceAllLiterally("\"", "")
      .replaceAll(" or [^\\s]+", "")
      .replaceAll("\\[([^\\]]+)\\]", "")
      .replaceAll("\\(([^\\]]+)\\)", "")
      .replaceAll(" by", "")
      .replaceAll(" previously [^\\s]+", "")
      .replaceAll(" aka [^\\s]+", "")
      .replaceAllLiterally("(me)", "")
      .replaceAll("[-.'\\s]+", "")
      .trim
  }

  def prefixOrSuffixOf(m1: String, m2: String) = {
    val fM1 = filterName(m1)
    val fM2 = filterName(m2)

    fM1.startsWith(fM2) || fM2.startsWith(fM1) ||
    fM1.endsWith(fM2) || fM2.endsWith(fM1)
  }
 }