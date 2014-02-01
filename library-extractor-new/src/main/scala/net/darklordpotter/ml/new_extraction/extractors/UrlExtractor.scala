package net.darklordpotter.ml.new_extraction.extractors

import net.darklordpotter.ml.new_extraction.LibraryExtractor.ExtractionContext
import net.darklordpotter.ml.new_extraction.ExtractionAction
import net.darklordpotter.ml.new_extraction.models.Url

/**
 * 2013-08-14
 * @author Michael Rose <michael@fullcontact.com>
 */
class UrlExtractor extends ExtractionAction {
  def apply(ctx: ExtractionContext) {
    urlPatterns.foreach { case (urlType, pattern) =>
      pattern.findAllMatchIn(ctx.story.pagetext).foreach { m =>
        ctx.elements += Url(urlType, m.toString())
      }
    }
  }

  val urlPatterns = Map(
    "FFN" -> "http://(?:www\\.)?fanfiction\\.net/s/([0-9]+)/([0-9]+)/?([a-z\\-_0-9]*)?".r,
    "PC" -> "http(?:s)?://(?:www\\.)?patronuscharm\\.net/s/([0-9]+)/([0-9]+)/?".r,
    "FICWAD" -> "http://(?:www\\.)?ficwad\\.com/story/([0-9]+)/?".r,
    "FICWAD_OLD" -> "http://(?:www\\.)?ficwad\\.com/viewstory\\.php\\?sid=([0-9]+)".r,
    "FFA" -> "http://([a-z0-9_-]+).fanficauthors.net/([a-z\\-_0-9]*)".r,
    "RS" -> "http://(?:www\\.)?restrictedsection.org/file\\.php\\?file=([0-9]+)".r
  )
}